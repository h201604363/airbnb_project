package uber;

import uber.config.kafka.KafkaProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

//import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ItemviewViewHandler {


    @Autowired
    private ItemviewRepository itemviewRepository;

    //////////////////////////////////////////
    // 방이 등록되었을 때 Insert -> Taxiview TABLE
    //////////////////////////////////////////
    @StreamListener(KafkaProcessor.INPUT)
    public void whenTaxiRegistered_then_CREATE_1 (@Payload ItemRegistered itemRegistered) {
        try {

            if (!itemRegistered.validate()) return;

            // view 객체 생성
            Itemview itemview = new Itemview();
            // view 객체에 이벤트의 Value 를 set 함
            itemview.setItemId(itemRegistered.getItemId());
            itemview.setDesc(itemRegistered.getDesc());
            itemview.setReviewCnt(itemRegistered.getReviewCnt());
            itemview.setItemStatus(itemRegistered.getStatus());
            // view 레파지 토리에 save
            itemviewRepository.save(itemview);
        
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    //////////////////////////////////////////////////
    // 방이 수정되었을 때 Update -> TaxiView TABLE
    //////////////////////////////////////////////////
    @StreamListener(KafkaProcessor.INPUT)
    public void whenItemModified_then_UPDATE_1(@Payload ItemModified itemModified) {
        try {
            if (!itemModified.validate()) return;
                // view 객체 조회
            Optional<Itemview> itemviewOptional = itemviewRepository.findById(itemModified.getItemId());
            if( itemviewOptional.isPresent()) {
                Itemview itemview = itemviewOptional.get();
                // view 객체에 이벤트의 eventDirectValue 를 set 함
                    itemview.setDesc(itemModified.getDesc());
                    itemview.setReviewCnt(itemModified.getReviewCnt());
                    itemview.setItemStatus(itemModified.getStatus());
                // view 레파지 토리에 save
                itemviewRepository.save(itemview);
            }
            
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //////////////////////////////////////////////////
    // 예약이 확정되었을 때 Update -> TaxiView TABLE
    //////////////////////////////////////////////////
    @StreamListener(KafkaProcessor.INPUT)
    public void whenReservationConfirmed_then_UPDATE_2(@Payload ReservationConfirmed reservationConfirmed) {
        try {
            if (!reservationConfirmed.validate()) return;
                // view 객체 조회
            Optional<Itemview> itemviewOptional = itemviewRepository.findById(reservationConfirmed.getItemId());
            if( itemviewOptional.isPresent()) {
                Itemview itemview = itemviewOptional.get();
                // view 객체에 이벤트의 eventDirectValue 를 set 함
                    itemview.setRsvId(reservationConfirmed.getRsvId());
                    itemview.setRsvStatus(reservationConfirmed.getStatus());
                // view 레파지 토리에 save
                itemviewRepository.save(itemview);
            }
            
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //////////////////////////////////////////////////
    // 결제가 승인 되었을 때 Update -> TaxiView TABLE
    //////////////////////////////////////////////////
    @StreamListener(KafkaProcessor.INPUT)
    public void whenPaymentApproved_then_UPDATE_3(@Payload PaymentApproved paymentApproved) {
        try {
            if (!paymentApproved.validate()) return;
                // view 객체 조회
            System.out.println("#######################");
            System.out.println("###PAYMENT APPROVED ###" + paymentApproved.getRsvId());
            System.out.println("#######################");

            //List<Taxiview> taxiviewList = taxiviewRepository.findByRsvId(paymentApproved.getRsvId());
            //for(Taxiview taxiview : taxiviewList){
            //    // view 객체에 이벤트의 eventDirectValue 를 set 함
            //    taxiview.setPayId(paymentApproved.getPayId());
            //    taxiview.setPayStatus(paymentApproved.getStatus());
            //    // view 레파지 토리에 save
            //    taxiviewRepository.save(taxiview);
            //}

            // TaxiId로 변경
            Optional<Itemview> itemviewOptional = itemviewRepository.findById(paymentApproved.getItemId());
            if( itemviewOptional.isPresent()) {
                Itemview itemview = itemviewOptional.get();
                itemview.setPayId(paymentApproved.getPayId());
                itemview.setPayStatus(paymentApproved.getStatus());
                itemviewRepository.save(itemview);
            }
            
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //////////////////////////////////////////////////
    // 예약이 취소 되었을 때 Update -> TaxiView TABLE
    //////////////////////////////////////////////////
    @StreamListener(KafkaProcessor.INPUT)
    public void whenReservationCancelled_then_UPDATE_4(@Payload ReservationCancelled reservationCancelled) {
        try {
            if (!reservationCancelled.validate()) return;
                // view 객체 조회
            List<Itemview> itemviewList = itemviewRepository.findByRsvId(reservationCancelled.getRsvId());
            for(Itemview taxiview : itemviewList){
                // view 객체에 이벤트의 eventDirectValue 를 set 함
                taxiview.setRsvStatus(reservationCancelled.getStatus());
                // view 레파지 토리에 save
                itemviewRepository.save(taxiview);
            }
            
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //////////////////////////////////////////////////
    // 결제가 취소 되었을 때 Update -> TaxiView TABLE
    //////////////////////////////////////////////////
    @StreamListener(KafkaProcessor.INPUT)
    public void whenPaymentCancelled_then_UPDATE_5(@Payload PaymentCancelled paymentCancelled) {
        try {
            if (!paymentCancelled.validate()) return;
                // view 객체 조회
            List<Itemview> itemviewList = itemviewRepository.findByPayId(paymentCancelled.getPayId());
            for(Itemview taxiview : itemviewList){
                // view 객체에 이벤트의 eventDirectValue 를 set 함
                taxiview.setPayStatus(paymentCancelled.getStatus());
                // view 레파지 토리에 save
                itemviewRepository.save(taxiview);
            }
            
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    
    //////////////////////////////////////////////////
    // 방이 예약 되었을 때 Update -> TaxiView TABLE
    //////////////////////////////////////////////////
    @StreamListener(KafkaProcessor.INPUT)
    public void whenTaxiReserved_then_UPDATE_6(@Payload ItemReserved itemReserved) {
    
        try {
            if (!itemReserved.validate()) return;
    
            System.out.println("#######################");
            System.out.println("###Taxi RESERVED ###" + itemReserved.getItemId());
            System.out.println("#######################");

            Optional<Itemview> itemviewOptional = itemviewRepository.findById(itemReserved.getItemId());
            if( itemviewOptional.isPresent()) {
                Itemview taxiview = itemviewOptional.get();
                taxiview.setItemStatus(itemReserved.getStatus());
                itemviewRepository.save(taxiview);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //////////////////////////////////////////////////
    // 방이 취소 되었을 때 Update -> TaxiView TABLE
    //////////////////////////////////////////////////
    @StreamListener(KafkaProcessor.INPUT)
    public void whenTaxiCancelled_then_UPDATE_6(@Payload ItemCancelled itemCancelled) {
    
        try {
            if (!itemCancelled.validate()) return;
    
            System.out.println("#######################");
            System.out.println("###Taxi CANCELLED ###" + itemCancelled.getItemId());
            System.out.println("#######################");

            Optional<Itemview> itemviewOptional = itemviewRepository.findById(itemCancelled.getItemId());
            if( itemviewOptional.isPresent()) {
                Itemview itemview = itemviewOptional.get();
                itemview.setItemStatus(itemCancelled.getStatus());
                itemviewRepository.save(itemview);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //////////////////////////////////////////////////
    // 방이 삭제 되었을 때 Delete -> TaxiView TABLE
    //////////////////////////////////////////////////
    @StreamListener(KafkaProcessor.INPUT)
    public void whenTaxiDeleted_then_DELETE_1(@Payload ItemDeleted itemDeleted) {
        try {
            if (!itemDeleted.validate()) return;
            // view 레파지 토리에 삭제 쿼리
            itemviewRepository.deleteById(itemDeleted.getItemId());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}