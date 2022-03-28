package uber;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

 @RestController
 public class TaxiController {

        @Autowired
        TaxiRepository TaxiRepository;

        @RequestMapping(value = "/check/chkAndReqReserve",
                        method = RequestMethod.GET,
                        produces = "application/json;charset=UTF-8")

        public boolean chkAndReqReserve(HttpServletRequest request, HttpServletResponse response) throws Exception {
                System.out.println("##### /check/chkAndReqReserve  called #####");

                // Parameter로 받은 TaxiID 추출
                long taxiId = Long.valueOf(request.getParameter("taxiId"));
                System.out.println("######################## chkAndReqReserve taxiId : " + taxiId);

                // TaxiID 데이터 조회
                Optional<Taxi> res = TaxiRepository.findById(taxiId);
                Taxi taxi = res.get(); // 조회한 ROOM 데이터
                System.out.println("######################## chkAndReqReserve taxi.getStatus() : " + taxi.getStatus());

                // taxi의 상태가 available이면 true
                boolean result = false;
                if(taxi.getStatus().equals("available")) {
                        result = true;
                } 

                System.out.println("######################## chkAndReqReserve Return : " + result);
                return result;
        }
 }
