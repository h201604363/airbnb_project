package uber;

import org.springframework.data.repository.CrudRepository;
//import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaxiviewRepository extends CrudRepository<Roomview, Long> {

    List<Roomview> findByRsvId(Long rsvId);
    List<Roomview> findByPayId(Long payId);

}