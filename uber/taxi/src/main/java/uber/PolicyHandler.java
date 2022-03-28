package uber;

import uber.config.kafka.KafkaProcessor;

import java.util.Optional;

//import com.fasterxml.jackson.databind.DeserializationFeature;
//import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class PolicyHandler{


    @Autowired
    private TaxiRepository taxiRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void onStringEventListener(@Payload String eventString){

    }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverReviewCreated_UpdateReviewCnt(@Payload ReviewCreated reviewCreated){

        if(reviewCreated.isMe()){

            ////////////////////////////////////////
            // 리뷰 등록 시 -> Taxi의 리뷰 카운트 증가
            ////////////////////////////////////////
            System.out.println("##### listener UpdateReviewCnt : " + reviewCreated.toJson());

            long taxiIdOfReview = reviewCreated.getTaxiId(); // 등록된 리뷰의 TaxiID

            updateReviewCnt(taxiIdOfReview, +1); // 리뷰건수 증가
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverReviewDeleted_UpdateReviewCnt(@Payload ReviewDeleted reviewDeleted){
        if(reviewDeleted.isMe()){
            ////////////////////////////////////////
            // 리뷰 삭제 시 -> Taxi의 리뷰 카운트 감소
            ////////////////////////////////////////
            System.out.println("##### listener UpdateReviewCnt : " + reviewDeleted.toJson());

            long taxiIdOfReview = reviewDeleted.getTaxiId(); // 삭제된 리뷰의 TaxiID

            updateReviewCnt(taxiIdOfReview, -1); // 리뷰건수 감소
        }
    }


    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverReservationConfirmed_ConfirmReserve(@Payload ReservationConfirmed reservationConfirmed){

        if(reservationConfirmed.isMe()){

            ////////////////////////////////////////////////////////////////////
            // 예약 확정 시 -> Taxi의 status => reserved, lastAction => reserved
            ////////////////////////////////////////////////////////////////////

            System.out.println("##### listener ConfirmReserve : " + reservationConfirmed.toJson());

            long taxiId = reservationConfirmed.getTaxiId(); // 삭제된 리뷰의 TaxiID

            updateTaxiStatus(taxiId, "reserved", "reserved"); // Status Update

        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverReservationCancelled_Cancel(@Payload ReservationCancelled reservationCancelled){

        if(reservationCancelled.isMe()){
            //////////////////////////////////////////////////////////////////////
            // 예약 취소 시 -> Taxi의 status => available, lastAction => cancelled
            //////////////////////////////////////////////////////////////////////
            System.out.println("##### listener Cancel : " + reservationCancelled.toJson());

            long taxiId = reservationCancelled.getTaxiId(); // 삭제된 리뷰의 TaxiID

            updateTaxiStatus(taxiId, "available", "cancelled"); // Status Update
        }
    }

    
    private void updateReviewCnt(long taxiId, long num)     {

        //////////////////////////////////////////////
        // taxiId 룸 데이터의 ReviewCnt를 num 만큼 가감
        //////////////////////////////////////////////

        // Taxi 테이블에서 taxiId의 Data 조회 -> taxi
        Optional<Taxi> res = taxiRepository.findById(taxiId);
        Taxi taxi = res.get();

        System.out.println("taxiId    : " + taxi.getTaxiId());
        System.out.println("reviewCnt : " + taxi.getReviewCnt());

        // taxi 값 수정
        taxi.setReviewCnt(taxi.getReviewCnt() + num); // 리뷰건수 증가/감소
        taxi.setLastAction("review");  // lastAction 값 셋팅

        System.out.println("Edited reviewCnt : " + taxi.getReviewCnt());
        System.out.println("Edited lastAction : " + taxi.getLastAction());

        /////////////
        // DB Update
        /////////////
        taxiRepository.save(taxi);

    }

        
    private void updateTaxiStatus(long taxiId, String status, String lastAction)     {

        //////////////////////////////////////////////
        // taxiId 룸 데이터의 status, lastAction 수정
        //////////////////////////////////////////////

        // Taxi 테이블에서 taxiId의 Data 조회 -> taxi
        Optional<Taxi> res = taxiRepository.findById(taxiId);
        Taxi taxi = res.get();

        System.out.println("taxiId      : " + taxi.getTaxiId());
        System.out.println("status      : " + taxi.getStatus());
        System.out.println("lastAction  : " + taxi.getLastAction());

        // taxi 값 수정
        taxi.setStatus(status); // status 수정 
        taxi.setLastAction(lastAction);  // lastAction 값 셋팅

        System.out.println("Edited status     : " + taxi.getStatus());
        System.out.println("Edited lastAction : " + taxi.getLastAction());

        /////////////
        // DB Update
        /////////////
        taxiRepository.save(taxi);

    }

}
