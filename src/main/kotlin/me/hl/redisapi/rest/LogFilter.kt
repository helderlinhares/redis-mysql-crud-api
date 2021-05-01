package me.hl.redisapi.rest

import org.slf4j.LoggerFactory
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Order(1)
@Component
class LogFilter : Filter {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain) {
        request as HttpServletRequest

        val logString = "${request.method} ${request.requestURI} "
        if (HttpStatus.valueOf((response as HttpServletResponse).status).is5xxServerError)
            logger.error(logString)
        else
            logger.info(logString)

        chain.doFilter(request, response)
    }

}