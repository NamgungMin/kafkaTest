package com.kakaobank.test.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log {

    private static Logger logger;

    private Log(){
        logger = LoggerFactory.getLogger(this.getClass());
    }

    private static class LazyHolser {
        public static final Log log = new Log();
    }

    public static Log getInstance() {
        return LazyHolser.log;
    }

    public void info(String mgs, Class c) {
       logger.info(c.getName() + " " + mgs);
    }

    public void debug(String mgs, Class c) {
        logger.debug(c.getName() + " " + mgs);
    }

    public void error(String mgs, Class c) {
        logger.error(c.getName() + " " + mgs);
    }

}
