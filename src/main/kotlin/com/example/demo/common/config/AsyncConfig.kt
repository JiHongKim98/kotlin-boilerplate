package com.example.demo.common.config

import com.example.demo.common.exception.AsyncExceptionHandler
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.AsyncConfigurer
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.Executor

@EnableAsync
@Configuration
class AsyncConfig(
    private val taskExecutionProperties: TaskExecutionProperties,
) : AsyncConfigurer {

    override fun getAsyncExecutor(): Executor = ThreadPoolTaskExecutor().apply {
        setThreadNamePrefix(THREAD_NAME_PREFIX)
        setAwaitTerminationSeconds(taskExecutionProperties.shutdown.awaitTerminationPeriod.seconds.toInt())
        setWaitForTasksToCompleteOnShutdown(taskExecutionProperties.shutdown.isAwaitTermination)
        maxPoolSize = taskExecutionProperties.pool.maxSize
        corePoolSize = taskExecutionProperties.pool.coreSize
        queueCapacity = taskExecutionProperties.pool.queueCapacity
        keepAliveSeconds = taskExecutionProperties.pool.keepAlive.seconds.toInt()
        initialize()
    }

    override fun getAsyncUncaughtExceptionHandler(): AsyncUncaughtExceptionHandler = AsyncExceptionHandler()

    companion object {
        private const val THREAD_NAME_PREFIX = "async-exec-"
    }
}
