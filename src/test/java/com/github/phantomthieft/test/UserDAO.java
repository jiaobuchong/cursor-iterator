package com.github.phantomthieft.test;

import static java.lang.Math.min;
import static java.util.stream.IntStream.range;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;

/**
 * @author w.vela
 */
public class UserDAO {

    private static final int MAX_USER_ID = 938;

    private final Logger logger = getLogger(getClass());

    // A fake DAO for test
    public List<User> getUsersAscById(Integer startId, int limit) {
        if (startId == null) {
            startId = 0;
        }
        List<User> result = range(startId, min(startId + limit, MAX_USER_ID)).mapToObj(User::new)
                .collect(Collectors.toList());
        logger.trace("get users asc by id, startId:" + startId + ", limit:" + limit + ", result:"
                + result);
        return result;
    }

    // A fake DAO for test
    public ScanResult scan(Integer startId, int limit) {
        if (startId == null) {
            startId = 0;
        }
        List<User> result = range(startId, min(startId + limit, MAX_USER_ID)).mapToObj(User::new)
                .collect(Collectors.toList());
        logger.trace("get users asc by id, startId:" + startId + ", limit:" + limit + ", result:"
                + result);
        Integer nextCursor = startId + limit > MAX_USER_ID ? null : startId + limit;
        return new ScanResult(result, nextCursor);
    }

    public ScanResult scan1(Integer startId, int limit) {
        if (startId == null) {
            startId = 0;
        }
        List<User> result = range(startId, min(startId + limit, MAX_USER_ID)).mapToObj(User::new)
                .collect(Collectors.toList());
        logger.trace("get users asc by id, startId:" + startId + ", limit:" + limit + ", result:"
                + result);
        // startId + limit > 105, 这个语句是没有毛病的，总的用户是 105 个，如果满足了这个条件:
        // startId + limit > 105, 说明已经没有数据了，在上一页就已经把数据取完了
        Integer nextCursor = startId + limit > 105 ? null : startId + limit;
        return new ScanResult(result, nextCursor);
    }

    public static final class ScanResult {

        private final List<User> users;
        private final Integer nextCursor;

        private ScanResult(List<User> users, Integer nextCursor) {
            this.users = users;
            this.nextCursor = nextCursor;
        }

        public List<User> getUsers() {
            return users;
        }

        public Integer getNextCursor() {
            return nextCursor;
        }

    }

}
