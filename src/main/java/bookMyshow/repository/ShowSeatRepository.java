package bookMyshow.repository;

import bookMyshow.model.ShowSeat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ShowSeatRepository {

    /*
     Simulating DB table:
     key   -> showSeatId
     value -> ShowSeat row
     */
    private final Map<String, ShowSeat> showSeatMap =
            new ConcurrentHashMap<>();

    public ShowSeat save(ShowSeat showSeat) {

        showSeatMap.put(
                showSeat.getId(),
                showSeat
        );

        return showSeat;
    }

    public List<ShowSeat> saveAll(
            List<ShowSeat> showSeats
    ) {

        for (ShowSeat seat : showSeats) {

            showSeatMap.put(
                    seat.getId(),
                    seat
            );
        }

        return showSeats;
    }

    public ShowSeat findById(String id) {

        return showSeatMap.get(id);
    }

    public List<ShowSeat> findByIds(
            List<String> ids
    ) {

        List<ShowSeat> result =
                new ArrayList<>();

        for (String id : ids) {

            ShowSeat seat =
                    showSeatMap.get(id);

            if (seat == null) {

                throw new IllegalArgumentException(
                        "Invalid ShowSeat Id: " + id
                );
            }

            result.add(seat);
        }

        return result;
    }

    /*
     Helpful for bootstrapping data
     */
    public void addAll(
            List<ShowSeat> showSeats
    ) {

        for (ShowSeat seat : showSeats) {

            showSeatMap.put(
                    seat.getId(),
                    seat
            );
        }
    }
}