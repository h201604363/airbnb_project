package uber;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
//import java.util.List;
//import java.util.Date;

@Entity
@Table(name="item_table")
public class Item {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long itemId;
    private String status;
    private String desc;
    private Long reviewCnt;
    private String lastAction;

    @PostPersist
    public void onPostPersist(){



        // 기본값 셋팅
        lastAction = "register";    // Insert는 항상 register
        reviewCnt = 0L;             // 리뷰 건수는 따로 입력이 들어오지 않아도 기본값 0
        status = "available";       // 최초 등록시 항상 이용가능

   
        ItemRegistered itemRegistered = new ItemRegistered();
        BeanUtils.copyProperties(this, itemRegistered);
        itemRegistered.publishAfterCommit();

    }

    @PostUpdate
    public void onPostUpdate(){

        System.out.println("lastAction : " + lastAction);


        if(lastAction.equals("modify") || lastAction.equals("review")) {
            ItemModified itemModified = new ItemModified();
            BeanUtils.copyProperties(this, itemModified);
            itemModified.publishAfterCommit();
        }


        if(lastAction.equals("reserved")) {
            ItemReserved itemReserved = new ItemReserved();
            BeanUtils.copyProperties(this, itemReserved);
            itemReserved.publishAfterCommit();
        }

     
        if(lastAction.equals("cancelled")) {
            ItemCancelled itemCancelled = new ItemCancelled();
            BeanUtils.copyProperties(this, itemCancelled);
            itemCancelled.publishAfterCommit();
        }


    }

    @PreRemove
    public void onPreRemove(){


      
        ItemDeleted itemDeleted = new ItemDeleted();
        BeanUtils.copyProperties(this, itemDeleted);
        itemDeleted.publishAfterCommit();

    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
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
