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
    private ItemRepository itemRepository;

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

            long itemIdOfReview = reviewCreated.getItemId(); // 등록된 리뷰의 TaxiID

            updateReviewCnt(itemIdOfReview, +1); // 리뷰건수 증가
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverReviewDeleted_UpdateReviewCnt(@Payload ReviewDeleted reviewDeleted){
        if(reviewDeleted.isMe()){
            ////////////////////////////////////////
            // 리뷰 삭제 시 -> Taxi의 리뷰 카운트 감소
            ////////////////////////////////////////
            System.out.println("##### listener UpdateReviewCnt : " + reviewDeleted.toJson());

            long itemIdOfReview = reviewDeleted.getItemId(); // 삭제된 리뷰의 TaxiID

            updateReviewCnt(itemIdOfReview, -1); // 리뷰건수 감소
        }
    }


    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverReservationConfirmed_ConfirmReserve(@Payload ReservationConfirmed reservationConfirmed){

        if(reservationConfirmed.isMe()){

            ////////////////////////////////////////////////////////////////////
            // 예약 확정 시 -> Taxi의 status => reserved, lastAction => reserved
            ////////////////////////////////////////////////////////////////////

            System.out.println("##### listener ConfirmReserve : " + reservationConfirmed.toJson());

            long itemId = reservationConfirmed.getItemId(); // 삭제된 리뷰의 TaxiID

            updateItemStatus(itemId, "reserved", "reserved"); // Status Update

        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverReservationCancelled_Cancel(@Payload ReservationCancelled reservationCancelled){

        if(reservationCancelled.isMe()){
            //////////////////////////////////////////////////////////////////////
            // 예약 취소 시 -> Taxi의 status => available, lastAction => cancelled
            //////////////////////////////////////////////////////////////////////
            System.out.println("##### listener Cancel : " + reservationCancelled.toJson());

            long itemId = reservationCancelled.getItemId(); // 삭제된 리뷰의 TaxiID

            updateItemStatus(itemId, "available", "cancelled"); // Status Update
        }
    }

    
    private void updateReviewCnt(long itemId, long num)     {

    
        Optional<Item> res = itemRepository.findById(itemId);
        Item item = res.get();

        System.out.println("itemId    : " + item.getItemId());
        System.out.println("reviewCnt : " + item.getReviewCnt());


        item.setReviewCnt(item.getReviewCnt() + num); // 리뷰건수 증가/감소
        item.setLastAction("review");  // lastAction 값 셋팅

        System.out.println("Edited reviewCnt : " + item.getReviewCnt());
        System.out.println("Edited lastAction : " + item.getLastAction());

        /////////////
        // DB Update
        /////////////
        itemRepository.save(item);

    }

        
    private void updateItemStatus(long itemId, String status, String lastAction)     {

    

        // Taxi 테이블에서 taxiId의 Data 조회 -> taxi
        Optional<Item> res = itemRepository.findById(itemId);
        Item item = res.get();

        System.out.println("itemId      : " + item.getItemId());
        System.out.println("status      : " + item.getStatus());
        System.out.println("lastAction  : " + item.getLastAction());

        // taxi 값 수정
        item.setStatus(status); // status 수정 
        item.setLastAction(lastAction);  // lastAction 값 셋팅

        System.out.println("Edited status     : " + item.getStatus());
        System.out.println("Edited lastAction : " + item.getLastAction());

        /////////////
        // DB Update
        /////////////
        itemRepository.save(item);

    }

}
