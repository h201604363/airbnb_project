package airbnb;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
//import java.util.List;
//import java.util.Date;

@Entity
@Table(name="taxi_table")
public class Taxi {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long taxiId;
    private String status;
    private String desc;
    private Long reviewCnt;
    private String lastAction;

    @PostPersist
    public void onPostPersist(){

        //////////////////////////////
        // Taxi 테이블 Insert 후 수행
        //////////////////////////////

        // 기본값 셋팅
        lastAction = "register";    // Insert는 항상 register
        reviewCnt = 0L;             // 리뷰 건수는 따로 입력이 들어오지 않아도 기본값 0
        status = "available";       // 최초 등록시 항상 이용가능

        // TaxiRegistered Event 발생
        TaxiRegistered taxiRegistered = new TaxiRegistered();
        BeanUtils.copyProperties(this, taxiRegistered);
        taxiRegistered.publishAfterCommit();

    }

    @PostUpdate
    public void onPostUpdate(){

        /////////////////////////////
        // Taxi 테이블 Update 후 수행
        /////////////////////////////

        System.out.println("lastAction : " + lastAction);

        // TaxiModified Event 발생 혹은 리뷰 이벤트 발생시
        if(lastAction.equals("modify") || lastAction.equals("review")) {
            TaxiModified taxiModified = new TaxiModified();
            BeanUtils.copyProperties(this, taxiModified);
            taxiModified.publishAfterCommit();
        }

        // TaxiReserved Event 발생
        if(lastAction.equals("reserved")) {
            TaxiReserved taxiReserved = new TaxiReserved();
            BeanUtils.copyProperties(this, taxiReserved);
            taxiReserved.publishAfterCommit();
        }

        // TaxiCancelled Event 발생
        if(lastAction.equals("cancelled")) {
            TaxiCancelled taxiCancelled = new TaxiCancelled();
            BeanUtils.copyProperties(this, taxiCancelled);
            taxiCancelled.publishAfterCommit();
        }

        // review 작성/삭제 시 -> Do Nothing
        //if(lastAction.equals("review")) {
            // Do Nothing
        //}
        
    }

    @PreRemove
    public void onPreRemove(){

        ////////////////////////////
        // Taxi 테이블 Delete 전 수행
        ////////////////////////////

        // TaxiDeleted Event 발생
        TaxiDeleted taxiDeleted = new TaxiDeleted();
        BeanUtils.copyProperties(this, taxiDeleted);
        taxiDeleted.publishAfterCommit();

    }

    public Long getTaxiId() {
        return taxiId;
    }

    public void setTaxiId(Long taxiId) {
        this.taxiId = taxiId;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
    public Long getReviewCnt() {
        return reviewCnt;
    }

    public void setReviewCnt(Long reviewCnt) {
        this.reviewCnt = reviewCnt;
    }
    public String getLastAction() {
        return lastAction;
    }

    public void setLastAction(String lastAction) {
        this.lastAction = lastAction;
    }
}
