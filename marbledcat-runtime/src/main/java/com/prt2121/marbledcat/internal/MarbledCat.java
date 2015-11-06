package com.prt2121.marbledcat.internal;

import android.os.Looper;
import android.util.Log;
import java.util.concurrent.ConcurrentSkipListSet;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * Created by pt2121 on 11/1/15.
 */
@Aspect public class MarbledCat {
  private static volatile boolean enabled = true;

  private final ConcurrentSkipListSet<Integer> lineSet = new ConcurrentSkipListSet<>();

  @Pointcut(value = "execution(* rx.functions.Func1.call(..))") public void func1Call() {
  }

  @Pointcut(value = "execution(* rx.functions.Func2.call(..))") public void func2Call() {
  }

  @Pointcut(value = "execution(* rx.functions.Action0.call(..))") public void action0Call() {
  }

  @Pointcut(value = "execution(* rx.functions.Action1.call(..))") public void action1Call() {
  }

  @Around("action0Call()") public Object aroundAction0Call(ProceedingJoinPoint joinPoint)
      throws Throwable {
    Object result = joinPoint.proceed();
    if (enabled) {
      StringBuilder builder = new StringBuilder();
      buildPrefix(joinPoint, builder);
      Signature signature = joinPoint.getSignature();
      Class<?> cls = signature.getDeclaringType();
      Log.v(asTag(cls), builder.append(" \u2601 ").toString());
    }
    return result;
  }

  @Around("action1Call()") public Object aroundAction1Call(ProceedingJoinPoint joinPoint)
      throws Throwable {
    return log(joinPoint);
  }

  @Around("func1Call()") public Object aroundFunc1Call(ProceedingJoinPoint joinPoint)
      throws Throwable {
    return log(joinPoint);
  }

  @Around("func2Call()") public Object aroundFunc2Call(ProceedingJoinPoint joinPoint)
      throws Throwable {
    return log(joinPoint);
  }

  private Object log(ProceedingJoinPoint joinPoint) throws Throwable {
    Object result = joinPoint.proceed();
    if (enabled) {
      StringBuilder builder = new StringBuilder();
      enter(joinPoint, builder);
      exit(joinPoint, result, builder);
    }
    return result;
  }

  private void enter(JoinPoint joinPoint, StringBuilder builder) {
    int line = buildPrefix(joinPoint, builder);
    int tab = lineSet.headSet(line).size();
    for (int i = 0; i < tab; i++) {
      builder.append("\t\u21e2  ");
    }
    CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
    Object[] parameterValues = joinPoint.getArgs();
    String[] parameterNames = codeSignature.getParameterNames();
    for (int i = 0; i < parameterValues.length; i++) {
      if (i > 0) {
        builder.append(", ");
      }
      builder.append(parameterNames[i]).append('=');
      builder.append(Strings.toString(parameterValues[i]));
    }
  }

  // add prefix (thread hash line) to StringBuilder and return line
  // i.e. RxComputationThreadPool-1	64f7292	54
  private int buildPrefix(JoinPoint joinPoint, StringBuilder builder) {
    if (Looper.myLooper() == Looper.getMainLooper()) {
      builder.append("MainThread               "); // 25 chars
    } else {
      builder.append(String.format("%25s", Thread.currentThread().getName()));
    }
    builder.append("\t");
    int hashCode = joinPoint.getTarget().hashCode();
    builder.append(String.format("%10s", Integer.toHexString(hashCode)));
    builder.append("\t");
    int line = joinPoint.getSourceLocation().getLine();
    builder.append(String.format("%4s", line));
    builder.append("\t");

    lineSet.add(line);
    return line;
  }

  private void exit(JoinPoint joinPoint, Object result, StringBuilder builder) {
    if (!enabled) return;

    Signature signature = joinPoint.getSignature();

    Class<?> cls = signature.getDeclaringType();
    boolean hasReturnType = signature instanceof MethodSignature
        && ((MethodSignature) signature).getReturnType() != void.class;

    if (hasReturnType) {
      builder.append(" \u21a6 ");
      builder.append(Strings.toString(result));
    }

    Log.v(asTag(cls), builder.toString());
  }

  private static String asTag(Class<?> cls) {
    if (cls.isAnonymousClass()) {
      return asTag(cls.getEnclosingClass());
    }
    return cls.getSimpleName();
  }

  public static void setEnabled(boolean enabled) {
    MarbledCat.enabled = enabled;
  }
}
