package uber;

import javax.persistence.*;
//import java.util.List;

@Entity
@Table(name="Itemview_table")
public class Itemview {

        @Id
        private Long itemId;
        private String desc;
        private Long reviewCnt;
        private String itemStatus;
        private Long rsvId;
        private String rsvStatus;
        private Long payId;
        private String payStatus;


        public Long getItemId() {
            return itemId;
        }
    
        public void setItemId(Long itemId) {
            this.itemId = itemId;
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

        public String getItemStatus() {
            return itemStatus;
        }

        public void setItemStatus(String itemStatus) {
            this.itemStatus = itemStatus;
        }

        public Long getRsvId() {
            return rsvId;
        }

        public void setRsvId(Long rsvId) {
            this.rsvId = rsvId;
        }

        public String getRsvStatus() {
            return rsvStatus;
        }

        public void setRsvStatus(String rsvStatus) {
            this.rsvStatus = rsvStatus;
        }

        public Long getPayId() {
            return payId;
        }

        public void setPayId(Long payId) {
            this.payId = payId;
        }

        public String getPayStatus() {
            return payStatus;
        }

        public void setPayStatus(String payStatus) {
            this.payStatus = payStatus;
        }

}
