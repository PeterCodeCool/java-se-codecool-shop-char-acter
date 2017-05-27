package com.codecool.shop;

import java.lang.invoke.MethodHandles;
import java.text.MessageFormat;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerTestClass {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static void main( String[] args )
    {
        String user = "john";
        String application = "gateway";
        LOGGER.info("Bad experience for user " + user + " at time " + Calendar.getInstance().getTime());
        LOGGER.info("Bad experience for user {}", user);
        LOGGER.info("Bad experience for user {}");
        LOGGER.info("Bad experience for user {} at time {}", user, Calendar.getInstance().getTime());
        LOGGER.info("Bad experience for user {} at time {} while accessing {}", user, Calendar.getInstance().getTime(), application);
        LOGGER.info("ERROR CODE \\{}; Bad experience for user {} at time {}", user, Calendar.getInstance().getTime());
        LOGGER.info("ERROR CODE {22}; Bad experience for user {} at time {}", user, Calendar.getInstance().getTime());

        if(LOGGER.isInfoEnabled()) {
            String message = MessageFormat.format("Bad experience for user {0} at time {1} while accessing {2}", user, Calendar.getInstance().getTime(), application);
            LOGGER.info(message);
        }
    }
}
