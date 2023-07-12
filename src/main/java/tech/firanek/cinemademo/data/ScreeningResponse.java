package tech.firanek.cinemademo.data;

import lombok.Value;
import tech.firanek.cinemademo.entity.Room;

import java.util.Collection;
import java.util.Map;

@Value
public class ScreeningResponse {
    Room room;
    Map<Integer, Collection<Integer>> availableSeatsInRows;
}
