package uber;

import org.springframework.data.repository.CrudRepository;
//import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemviewRepository extends CrudRepository<Itemview, Long> {

    List<Itemview> findByRsvId(Long rsvId);
    List<Itemview> findByPayId(Long payId);

}