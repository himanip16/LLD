package bookMyshow;

import bookMyshow.model.Audi;
import bookMyshow.model.Movie;
import bookMyshow.model.Show;
import bookMyshow.model.Theatre;

import java.util.List;

public class BookMyShowMain {
    public static void main(String[] args) {
        Theatre theatre = new Theatre();
        Audi audi = new Audi();
        theatre.setAudiList(List.of(audi));
        Show show = new Show();
        Movie movie = new Movie();
    }
}
