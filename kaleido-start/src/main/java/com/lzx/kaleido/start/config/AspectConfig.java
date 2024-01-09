package com.lzx.kaleido.start.config;

import com.lzx.kaleido.domain.api.annotations.LogRecord;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.autoproxy.AbstractBeanFactoryAwareAdvisingPostProcessor;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author lwp
 * @date 2024-01-16
 **/
@Slf4j
@Configuration(proxyBeanMethods = false)
public class AspectConfig {
    
    @Bean
    public AopAnnotationPostProcessor aopAnnotationPostProcessor() {
        return new AopAnnotationPostProcessor(new AopMethodInterceptor(LogRecord.class));
    }
    
    
    public static class AopAnnotationPostProcessor extends AbstractBeanFactoryAwareAdvisingPostProcessor {
        
        private final AopMethodInterceptor aopMethodInterceptor;
        
        public AopAnnotationPostProcessor(AopMethodInterceptor aopMethodInterceptor) {
            this.aopMethodInterceptor = aopMethodInterceptor;
        }
        
        @Override
        public void setBeanFactory(BeanFactory beanFactory) {
            super.setBeanFactory(beanFactory);
            this.advisor = new AopPointcutAdvisor(aopMethodInterceptor, aopMethodInterceptor.getAnnotationTypes());
        }
    }
    
    
    public static class AopMethodInterceptor implements MethodInterceptor, Ordered {
        
        private final Set<Class<? extends Annotation>> asyncAnnotationTypes;
        
        @SafeVarargs
        public AopMethodInterceptor(Class<? extends Annotation>... annotationTypes) {
            this.asyncAnnotationTypes = Arrays.stream(annotationTypes).collect(Collectors.toSet());
        }
        
        @Override
        public Object invoke(final MethodInvocation invocation) throws Throwable {
            return invocation.proceed();
        }
        
        public Set<Class<? extends Annotation>> getAnnotationTypes() {
            return asyncAnnotationTypes;
        }
        
        @Override
        public int getOrder() {
            return 0;
        }
        
    }
    
    @Getter
    public static class AopPointcutAdvisor extends AbstractPointcutAdvisor {
        
        private final Advice advice;
        
        private final Pointcut pointcut;
        
        public AopPointcutAdvisor(Advice advice, Set<Class<? extends Annotation>> asyncAnnotationTypes) {
            this.advice = advice;
            this.pointcut = buildPointcut(asyncAnnotationTypes);
        }
        
        private Pointcut buildPointcut(Set<Class<? extends Annotation>> asyncAnnotationTypes) {
            ComposablePointcut result = null;
            for (Class<? extends Annotation> asyncAnnotationType : asyncAnnotationTypes) {
                Pointcut cpc = new AnnotationMatchingPointcut(asyncAnnotationType, true);
                Pointcut mpc = AnnotationMatchingPointcut.forMethodAnnotation(asyncAnnotationType);
                if (result == null) {
                    result = new ComposablePointcut(cpc).union(mpc);
                } else {
                    result.union(cpc).union(mpc);
                }
            }
            return result;
        }
    }
}
