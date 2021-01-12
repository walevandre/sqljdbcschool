package com.ua.foxminded.controller.service.testdata;

import com.ua.foxminded.controller.dao.GroupsDao;
import com.ua.foxminded.controller.dao.exceptions.DAOException;
import com.ua.foxminded.domain.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class GroupsGenerator implements Generator<GroupsDao> {

    private static final int ASCII_NUMBER_FROM = 48;
    private static final int ASCII_NUMBER_TO = 57;
    private static final int ASCII_CHARACTER_FROM = 97;
    private static final int ASCII_CHARACTER_TO = 122;
    private static final String HYPHEN = "-";
    private static final int LENGTH_RANDOM_STRING = 2;
    private static final int LENGTH_LIST_GROUPS = 9;

    private static final Logger log = LoggerFactory.getLogger(GroupsGenerator.class);

    @Override
    public void generateRandomly(GroupsDao groupsDao) throws GeneratorDataException {

        List<Group> groups = new ArrayList<>();
        Set<String> groupsSet = new HashSet<>();

        while (groupsSet.size() <= LENGTH_LIST_GROUPS) {
            groupsSet.add(getRandomString(ASCII_CHARACTER_FROM, ASCII_CHARACTER_TO)
                    + HYPHEN
                    + getRandomString(ASCII_NUMBER_FROM, ASCII_NUMBER_TO));
        }

        groupsSet.forEach(value -> groups.add(new Group().name(value)));
        try {
            if (!groups.isEmpty() && groups != null) {
                groupsDao.saveList(groups);
            } else {
                log.debug("Empty list of groups to save in DB");
                throw new GeneratorDataException("Empty list of groups to save in DB");
            }
        } catch (DAOException e) {
            log.error("Failed to generate groups.", e);
            throw new GeneratorDataException("Failed to generate groups.", e);
        }
    }

    @Override
    public void generateFromFile(String fileName, GroupsDao groupsDao) throws GeneratorDataException {
        log.warn("Unsupported operation");
        throw new GeneratorDataException("Unsupported operation");
    }

    private String getRandomString(int start, int end) {

        int length = LENGTH_RANDOM_STRING;
        Random r = new Random();
        String s = r.ints(start, end)
                .mapToObj(i -> (char) i)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
        return s;
    }
}
