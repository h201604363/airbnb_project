package uber;

import javax.persistence.*;
//import java.util.List;

@Entity
@Table(name="Taxiview_table")
public class Taxiview {

        @Id
        private Long taxiId;
        private String desc;
        private Long reviewCnt;
        private String taxiStatus;
        private Long rsvId;
        private String rsvStatus;
        private Long payId;
        private String payStatus;


        public Long getTaxiId() {
            return taxiId;
        }

        public void setTaxiId(Long taxiId) {
            this.taxiId = taxiId;
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

        public String getTaxiStatus() {
            return taxiStatus;
        }

        public void setTaxiStatus(String taxiStatus) {
            this.taxiStatus = taxiStatus;
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
