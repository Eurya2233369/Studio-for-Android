package com.eurya.javaide.compiler;

import org.openjdk.source.util.JavacTask;
import org.openjdk.source.util.TaskEvent;
import org.openjdk.source.util.TaskListener;
import org.openjdk.tools.javac.api.JavacTaskImpl;
import org.openjdk.tools.javac.api.JavacTool;
import org.openjdk.tools.javac.api.JavacTrees;
import org.openjdk.tools.javac.api.MultiTaskListener;
import org.openjdk.tools.javac.code.Types;
import org.openjdk.tools.javac.comp.Annotate;
import org.openjdk.tools.javac.comp.Check;
import org.openjdk.tools.javac.comp.CompileStates;
import org.openjdk.tools.javac.comp.Enter;
import org.openjdk.tools.javac.comp.Modules;
import org.openjdk.tools.javac.main.Arguments;
import org.openjdk.tools.javac.model.JavacElements;
import org.openjdk.tools.javac.util.Context;
import org.openjdk.tools.javac.util.DefinedBy;
import org.openjdk.tools.javac.util.Log;
import org.openjdk.tools.javac.main.JavaCompiler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.openjdk.javax.tools.Diagnostic;
import org.openjdk.javax.tools.DiagnosticListener;
import org.openjdk.javax.tools.JavaFileManager;
import org.openjdk.javax.tools.JavaFileObject;

/**
 * A pool of reusable JavacTasks. When a task is no valid anymore, it is returned to the pool, and its Context may be
 * reused for future processing in some cases. The reuse is achieved by replacing some components (most notably
 * JavaCompiler and Log) with reusable counterparts, and by cleaning up leftovers from previous compilation.
 *
 * <p>For each combination of options, a separate task/context is created and kept, as most option values are cached
 * inside components themselves.
 *
 * <p>When the compilation redefines sensitive classes (e.g. classes in the the java.* packages), the task/context is
 * not reused.
 *
 * <p>When the task is reused, then packages that were already listed won't be listed again.
 *
 * <p>Care must be taken to only return tasks that won't be used by the original caller.
 *
 * <p>Care must also be taken when custom components are installed, as those are not cleaned when the task/context is
 * reused, and subsequent getTask may return a task based on a context with these custom components.
 *
 * <p><b>This is NOT part of any supported API. If you write code that depends on this, you do so at your own risk. This
 * code and its internal interfaces are subject to change or deletion without notice.</b>
 */

public class ReusableCompiler {

    private static final Logger LOG = Logger.getLogger("main");
    private static final JavacTool systemProvider = JavacTool.create();

    private List<String> currentOptions = new ArrayList<>();
    private ReusableContext currentContext;
    private boolean checkedOut;

    /**
     * Creates a new task as if by JavaCompiler and runs the provided worker with it. The
     * task is only valid while the worker is running. The internal structures may be reused from some previous
     * compilation.
     *
     * @param fileManager a file manager; if {@code null} use the compiler's standard filemanager
     * @param diagnosticListener a diagnostic listener; if {@code null} use the compiler's default method for reporting
     *     diagnostics
     * @param options compiler options, {@code null} means no options
     * @param classes names of classes to be processed by annotation processing, {@code null} means no class names
     * @param compilationUnits the compilation units to compile, {@code null} means no compilation units
     * @return an object representing the compilation
     * @throws RuntimeException if an unrecoverable error occurred in a user supplied component. The {@linkplain
     *     Throwable#getCause() cause} will be the error in user code.
     * @throws IllegalArgumentException if any of the options are invalid, or if any of the given compilation units are
     *     of other kind than {@linkplain JavaFileObject.Kind#SOURCE source}
     */
    public Borrow getTask(
		JavaFileManager fileManager,
		DiagnosticListener<? super JavaFileObject> diagnosticListener,
		Iterable<String> options,
		Iterable<String> classes,
		Iterable<? extends JavaFileObject> compilationUnits) {
        if (checkedOut) {
            throw new RuntimeException("Compiler is already in-use!");
        }
        checkedOut = true;
        List<String> opts =
			StreamSupport.stream(options.spliterator(), false).collect(Collectors.toCollection(ArrayList::new));
        if (!opts.equals(currentOptions)) {
            LOG.warning(String.format("Options changed from %s to %s, creating new compiler", options, opts));
            currentOptions = opts;
            currentContext = new ReusableContext(opts);
        }
        JavacTaskImpl task =
			(JavacTaskImpl)
			systemProvider.getTask(
			null, fileManager, diagnosticListener, opts, classes, compilationUnits, currentContext);

        task.addTaskListener(currentContext);
        return new Borrow(task, currentContext);
    }

    public class Borrow implements AutoCloseable {
        final JavacTask task;
        boolean closed;

        Borrow(JavacTask task, ReusableContext ctx) {
            this.task = task;
        }

        @Override
        public void close() {
            if (closed) return;
            // not returning the context to the pool if task crashes with an exception
            // the task/context may be in a broken state
            currentContext.clear();
            try {
                Method method = JavacTaskImpl.class.getDeclaredMethod("cleanup");
                method.setAccessible(true);
                method.invoke(task);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            checkedOut = false;
            closed = true;
        }
    }

    static class ReusableContext extends Context implements TaskListener {

        List<String> arguments;

        ReusableContext(List<String> arguments) {
            super();
            this.arguments = arguments;
            put(Log.logKey, ReusableLog.factory);
            put(JavaCompiler.compilerKey, ReusableJavaCompiler.factory);
        }

        public void clear() {
            drop(Arguments.argsKey);
            drop(DiagnosticListener.class);
            drop(Log.outKey);
            drop(Log.errKey);
            drop(JavaFileManager.class);
            drop(JavacTask.class);
            drop(JavacTrees.class);
            drop(JavacElements.class);

            if (ht.get(Log.logKey) instanceof ReusableLog) {
                // log already inited - not first round
                ((ReusableLog) Log.instance(this)).clear();
                Enter.instance(this).newRound();
                ((ReusableJavaCompiler) ReusableJavaCompiler.instance(this)).clear();
                Types.instance(this).newRound();
                Check.instance(this).newRound();
                Modules.instance(this).newRound();
                Annotate.instance(this).newRound();
                CompileStates.instance(this).clear();
                MultiTaskListener.instance(this).clear();
            }
        }

        @Override
        @DefinedBy(DefinedBy.Api.COMPILER_TREE)
        public void finished(TaskEvent e) {
            // do nothing
        }

        @Override
        @DefinedBy(DefinedBy.Api.COMPILER_TREE)
        public void started(TaskEvent e) {
            // do nothing
        }

        <T> void drop(Key<T> k) {
            ht.remove(k);
        }

        <T> void drop(Class<T> c) {
            ht.remove(key(c));
        }

        /**
         * Reusable JavaCompiler; exposes a method to clean up the component from leftovers associated with previous
         * compilations.
         */
        static class ReusableJavaCompiler extends JavaCompiler {

            static final Factory<JavaCompiler> factory = ReusableJavaCompiler::new;

            ReusableJavaCompiler(Context context) {
                super(context);
            }

            @Override
            public void close() {
                // do nothing
            }

            void clear() {
                newRound();
            }

            @Override
            protected void checkReusable() {
                // do nothing - it's ok to reuse the compiler
            }
        }

        /**
         * Reusable Log; exposes a method to clean up the component from leftovers associated with previous
         * compilations.
         */
        static class ReusableLog extends Log {

            static final Factory<Log> factory = ReusableLog::new;

            Context context;

            ReusableLog(Context context) {
                super(context);
                this.context = context;
            }

            void clear() {
                recorded.clear();
                sourceMap.clear();
                nerrors = 0;
                nwarnings = 0;
                // Set a fake listener that will lazily lookup the context for the 'real' listener. Since
                // this field is never updated when a new task is created, we cannot simply reset the field
                // or keep old value. This is a hack to workaround the limitations in the current infrastructure.
                diagListener =
					new DiagnosticListener<JavaFileObject>() {
					DiagnosticListener<JavaFileObject> cachedListener;

					@Override
					@DefinedBy(DefinedBy.Api.COMPILER)
					@SuppressWarnings("unchecked")
					public void report(Diagnostic<? extends JavaFileObject> diagnostic) {
						if (cachedListener == null) {
							cachedListener = context.get(DiagnosticListener.class);
						}
						cachedListener.report(diagnostic);
					}
				};
            }
        }
    }
}
