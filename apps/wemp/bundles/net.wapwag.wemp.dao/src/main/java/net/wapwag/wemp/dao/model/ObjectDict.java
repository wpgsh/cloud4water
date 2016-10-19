package net.wapwag.wemp.dao.model;

import javax.persistence.*;

/**
 * Object dictionary for define all type of the object
 * Created by Administrator on 2016/10/17 0017.
 */
@Entity
@Table(name = "object_dict")
public class ObjectDict {


    @Id
    @Column(name = "dict_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int dictId;

    @Column(name = "dict_type")
    private String dictType;

    @Column(name = "dict_name")
    private String dictName;

    @Column(name = "dict_text")
    private String dictText;

    @Column(name = "dict_value")
    private String dictValue;

    @Column(name = "dict_remark")
    private String dictRemark;

    @Column(name = "dict_order")
    private int dictOrder;

    @Column(name = "dict_create_date", insertable = false)
    private String dictCreateDate;

    @Column(name = "dict_update_date", insertable = false)
    private String dictUpdateDate;

    public int getDictId() {
        return dictId;
    }

    public void setDictId(int dictId) {
        this.dictId = dictId;
    }

    public String getDictType() {
        return dictType;
    }

    public void setDictType(String dictType) {
        this.dictType = dictType;
    }

    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    public String getDictText() {
        return dictText;
    }

    public void setDictText(String dictText) {
        this.dictText = dictText;
    }

    public String getDictValue() {
        return dictValue;
    }

    public void setDictValue(String dictValue) {
        this.dictValue = dictValue;
    }

    public String getDictRemark() {
        return dictRemark;
    }

    public void setDictRemark(String dictRemark) {
        this.dictRemark = dictRemark;
    }

    public int getDictOrder() {
        return dictOrder;
    }

    public void setDictOrder(int dictOrder) {
        this.dictOrder = dictOrder;
    }

    public String getDictCreateDate() {
        return dictCreateDate;
    }

    public void setDictCreateDate(String dictCreateDate) {
        this.dictCreateDate = dictCreateDate;
    }

    public String getDictUpdateDate() {
        return dictUpdateDate;
    }

    public void setDictUpdateDate(String dictUpdateDate) {
        this.dictUpdateDate = dictUpdateDate;
    }
}
