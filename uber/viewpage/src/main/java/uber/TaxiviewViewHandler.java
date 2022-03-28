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
public class TaxiviewViewHandler {


    @Autowired
    private TaxiviewRepository taxiviewRepository;

    //////////////////////////////////////////
    // 방이 등록되었을 때 Insert -> Taxiview TABLE
    //////////////////////////////////////////
    @StreamListener(KafkaProcessor.INPUT)
    public void whenTaxiRegistered_then_CREATE_1 (@Payload TaxiRegistered taxiRegistered) {
        try {

            if (!taxiRegistered.validate()) return;

            // view 객체 생성
            Taxiview taxiview = new Taxiview();
            // view 객체에 이벤트의 Value 를 set 함
            taxiview.setTaxiId(taxiRegistered.getTaxiId());
            taxiview.setDesc(taxiRegistered.getDesc());
            taxiview.setReviewCnt(taxiRegistered.getReviewCnt());
            taxiview.setTaxiStatus(taxiRegistered.getStatus());
            // view 레파지 토리에 save
            taxiviewRepository.save(taxiview);
        
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    //////////////////////////////////////////////////
    // 방이 수정되었을 때 Update -> TaxiView TABLE
    //////////////////////////////////////////////////
    @StreamListener(KafkaProcessor.INPUT)
    public void whenTaxiModified_then_UPDATE_1(@Payload TaxiModified taxiModified) {
        try {
            if (!taxiModified.validate()) return;
                // view 객체 조회
            Optional<Taxiview> taxiviewOptional = taxiviewRepository.findById(taxiModified.getTaxiId());
            if( taxiviewOptional.isPresent()) {
                Taxiview taxiview = taxiviewOptional.get();
                // view 객체에 이벤트의 eventDirectValue 를 set 함
                    taxiview.setDesc(taxiModified.getDesc());
                    taxiview.setReviewCnt(taxiModified.getReviewCnt());
                    taxiview.setTaxiStatus(taxiModified.getStatus());
                // view 레파지 토리에 save
                taxiviewRepository.save(taxiview);
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
            Optional<Taxiview> taxiviewOptional = taxiviewRepository.findById(reservationConfirmed.getTaxiId());
            if( taxiviewOptional.isPresent()) {
                Taxiview taxiview = taxiviewOptional.get();
                // view 객체에 이벤트의 eventDirectValue 를 set 함
                    taxiview.setRsvId(reservationConfirmed.getRsvId());
                    taxiview.setRsvStatus(reservationConfirmed.getStatus());
                // view 레파지 토리에 save
                taxiviewRepository.save(taxiview);
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
            Optional<Taxiview> taxiviewOptional = taxiviewRepository.findById(paymentApproved.getTaxiId());
            if( taxiviewOptional.isPresent()) {
                Taxiview taxiview = taxiviewOptional.get();
                taxiview.setPayId(paymentApproved.getPayId());
                taxiview.setPayStatus(paymentApproved.getStatus());
                taxiviewRepository.save(taxiview);
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
            List<Taxiview> taxiviewList = taxiviewRepository.findByRsvId(reservationCancelled.getRsvId());
            for(Taxiview taxiview : taxiviewList){
                // view 객체에 이벤트의 eventDirectValue 를 set 함
                taxiview.setRsvStatus(reservationCancelled.getStatus());
                // view 레파지 토리에 save
                taxiviewRepository.save(taxiview);
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
            List<Taxiview> taxiviewList = taxiviewRepository.findByPayId(paymentCancelled.getPayId());
            for(Taxiview taxiview : taxiviewList){
                // view 객체에 이벤트의 eventDirectValue 를 set 함
                taxiview.setPayStatus(paymentCancelled.getStatus());
                // view 레파지 토리에 save
                taxiviewRepository.save(taxiview);
            }
            
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    
    //////////////////////////////////////////////////
    // 방이 예약 되었을 때 Update -> TaxiView TABLE
    //////////////////////////////////////////////////
    @StreamListener(KafkaProcessor.INPUT)
    public void whenTaxiReserved_then_UPDATE_6(@Payload TaxiReserved taxiReserved) {
    
        try {
            if (!taxiReserved.validate()) return;
    
            System.out.println("#######################");
            System.out.println("###Taxi RESERVED ###" + taxiReserved.getTaxiId());
            System.out.println("#######################");

            Optional<Taxiview> taxiviewOptional = taxiviewRepository.findById(taxiReserved.getTaxiId());
            if( taxiviewOptional.isPresent()) {
                Taxiview taxiview = taxiviewOptional.get();
                taxiview.setTaxiStatus(taxiReserved.getStatus());
                taxiviewRepository.save(taxiview);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //////////////////////////////////////////////////
    // 방이 취소 되었을 때 Update -> TaxiView TABLE
    //////////////////////////////////////////////////
    @StreamListener(KafkaProcessor.INPUT)
    public void whenTaxiCancelled_then_UPDATE_6(@Payload TaxiCancelled taxiCancelled) {
    
        try {
            if (!taxiCancelled.validate()) return;
    
            System.out.println("#######################");
            System.out.println("###Taxi CANCELLED ###" + taxiCancelled.getTaxiId());
            System.out.println("#######################");

            Optional<Taxiview> taxiviewOptional = taxiviewRepository.findById(taxiCancelled.getTaxiId());
            if( taxiviewOptional.isPresent()) {
                Taxiview taxiview = taxiviewOptional.get();
                taxiview.setTaxiStatus(taxiCancelled.getStatus());
                taxiviewRepository.save(taxiview);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //////////////////////////////////////////////////
    // 방이 삭제 되었을 때 Delete -> TaxiView TABLE
    //////////////////////////////////////////////////
    @StreamListener(KafkaProcessor.INPUT)
    public void whenTaxiDeleted_then_DELETE_1(@Payload TaxiDeleted taxiDeleted) {
        try {
            if (!taxiDeleted.validate()) return;
            // view 레파지 토리에 삭제 쿼리
            taxiviewRepository.deleteById(taxiDeleted.getTaxiId());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}