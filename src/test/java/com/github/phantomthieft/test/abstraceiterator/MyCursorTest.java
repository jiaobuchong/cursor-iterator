package com.github.phantomthieft.test.abstraceiterator;

import static java.util.Spliterator.IMMUTABLE;
import static java.util.Spliterator.NONNULL;
import static java.util.Spliterator.ORDERED;
import static java.util.Spliterators.spliteratorUnknownSize;
import static java.util.stream.Collectors.toList;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import com.github.phantomthief.util.CursorIterator;
import com.github.phantomthieft.test.User;
import com.github.phantomthieft.test.UserDAO;

/**
 * Created on 2019-06-17
 */
public class MyCursorTest {
    private final Logger logger = getLogger(getClass());

    @Test
    void testGenericBuilder() {
        UserDAO userDAO = new UserDAO();

        // 在 app 的下拉列表中，传入下面两个参数，则可实现无限下拉
        // 很通用的一种模式
        Integer startId = 100;
        int countPerFetch = 10;

        //        Arrays.asList.<Snow>()
        //        <Integer, User> 中插入一条"线索"，以告诉编译器对于由
        //        Arrays.asList() 产生的 List 类型，实际的目标类型应该是什么，这称为显示类型参数说明
        // Iterator 接口有两个方法 hasNext 和 next 方法，需要实现
        CursorIterator<Integer, User> users = CursorIterator.<Integer, User> newGenericBuilder() //
                .start(startId) //
                .cursorExtractor(User::getId) //
                .bufferSize(countPerFetch) //
                .build(userDAO::getUsersAscById);

        //        https://www.ibm.com/developerworks/cn/java/j-java-streams-3-brian-goetz/index.html
        List<User> collect = users.stream() //
                //                .filter(user -> user.getId() % 11 == 0) //
                //        保留一个已看到多少元素的计数器，在这之后丢弃任何元素。
                //                .limit(countPerFetch) // limit 的数据大小如果和 bufferSize 一样的话，只会查询一次
                .collect(toList());
        collect.forEach(u -> logger.info("user:{}", u));
    }

    @Test
    void testIteratorPages() {
        // 针对分页处理数据的这种情况
        Integer startId = 100;
        int countPerFetch = 10;
        UserDAO userDAO = new UserDAO();

        CursorIterator<Integer, User> users = CursorIterator.<Integer, User> newGenericBuilder() //
                .start(startId) //
                .cursorExtractor(User::getId) //
                .bufferSize(countPerFetch) //
                .build(userDAO::getUsersAscById);
        Iterator<User> userIterator = users.iterator();
        while (userIterator.hasNext()) {
            logger.info("user: {}", userIterator.next());
        }
    }

    @Test
    public void testIterate() {
        //        return StreamSupport.stream(spliteratorUnknownSize(iterator(), (NONNULL | IMMUTABLE | ORDERED)), false);
        Stream.iterate(1,item -> item + 2).limit(10).forEach(System.out::println);
    }

    @Test
    public void testIterator() {

        Iterator iterator = new Iterator() {
            int i = 0;
            @Override
            public boolean hasNext() {
                return i < 5;
            }

            @Override
            public Object next() {
                i++;
                if (i == 4) {
                    return null;
                } else {
                    return i;
                }
            }
        };
        StreamSupport.stream(spliteratorUnknownSize(iterator, (NONNULL | IMMUTABLE | ORDERED)), false).forEach(
                o -> System.out.println(o)
        );
    }

}
