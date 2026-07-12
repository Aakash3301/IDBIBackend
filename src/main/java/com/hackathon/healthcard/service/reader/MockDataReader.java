package com.hackathon.healthcard.service.reader;

public interface MockDataReader<T> {
    T readData(String msmeId);
}
