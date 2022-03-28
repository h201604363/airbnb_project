package uber;

import org.springframework.data.repository.CrudRepository;
//import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaxiviewRepository extends CrudRepository<Taxiview, Long> {

    List<Taxiview> findByRsvId(Long rsvId);
    List<Taxiview> findByPayId(Long payId);

}