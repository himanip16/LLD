package bookMyshow.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class Show {
    private String id;
    private String name;
    private Audi audi;
    private Movie movie;
    private LocalDateTime start;
    private LocalDateTime end;
}
