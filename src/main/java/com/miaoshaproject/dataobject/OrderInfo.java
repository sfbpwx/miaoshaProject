package com.miaoshaproject.dataobject;

import java.math.BigDecimal;
import java.util.Date;

public class OrderInfo {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_info.id
     *
     * @mbggenerated Wed Oct 30 18:48:53 CST 2019
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_info.user_id
     *
     * @mbggenerated Wed Oct 30 18:48:53 CST 2019
     */
    private Long userId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_info.goods_id
     *
     * @mbggenerated Wed Oct 30 18:48:53 CST 2019
     */
    private Long goodsId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_info.delivery_addr_id
     *
     * @mbggenerated Wed Oct 30 18:48:53 CST 2019
     */
    private Long deliveryAddrId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_info.goods_name
     *
     * @mbggenerated Wed Oct 30 18:48:53 CST 2019
     */
    private String goodsName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_info.goods_count
     *
     * @mbggenerated Wed Oct 30 18:48:53 CST 2019
     */
    private Integer goodsCount;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_info.goods_price
     *
     * @mbggenerated Wed Oct 30 18:48:53 CST 2019
     */
    private BigDecimal goodsPrice;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_info.order_channel
     *
     * @mbggenerated Wed Oct 30 18:48:53 CST 2019
     */
    private Byte orderChannel;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_info.status
     *
     * @mbggenerated Wed Oct 30 18:48:53 CST 2019
     */
    private Byte status;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_info.create_date
     *
     * @mbggenerated Wed Oct 30 18:48:53 CST 2019
     */
    private Date createDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_info.pay_date
     *
     * @mbggenerated Wed Oct 30 18:48:53 CST 2019
     */
    private Date payDate;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_info.id
     *
     * @return the value of order_info.id
     *
     * @mbggenerated Wed Oct 30 18:48:53 CST 2019
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_info.id
     *
     * @param id the value for order_info.id
     *
     * @mbggenerated Wed Oct 30 18:48:53 CST 2019
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_info.user_id
     *
     * @return the value of order_info.user_id
     *
     * @mbggenerated Wed Oct 30 18:48:53 CST 2019
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_info.user_id
     *
     * @param userId the value for order_info.user_id
     *
     * @mbggenerated Wed Oct 30 18:48:53 CST 2019
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_info.goods_id
     *
     * @return the value of order_info.goods_id
     *
     * @mbggenerated Wed Oct 30 18:48:53 CST 2019
     */
    public Long getGoodsId() {
        return goodsId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_info.goods_id
     *
     * @param goodsId the value for order_info.goods_id
     *
     * @mbggenerated Wed Oct 30 18:48:53 CST 2019
     */
    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_info.delivery_addr_id
     *
     * @return the value of order_info.delivery_addr_id
     *
     * @mbggenerated Wed Oct 30 18:48:53 CST 2019
     */
    public Long getDeliveryAddrId() {
        return deliveryAddrId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_info.delivery_addr_id
     *
     * @param deliveryAddrId the value for order_info.delivery_addr_id
     *
     * @mbggenerated Wed Oct 30 18:48:53 CST 2019
     */
    public void setDeliveryAddrId(Long deliveryAddrId) {
        this.deliveryAddrId = deliveryAddrId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_info.goods_name
     *
     * @return the value of order_info.goods_name
     *
     * @mbggenerated Wed Oct 30 18:48:53 CST 2019
     */
    public String getGoodsName() {
        return goodsName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_info.goods_name
     *
     * @param goodsName the value for order_info.goods_name
     *
     * @mbggenerated Wed Oct 30 18:48:53 CST 2019
     */
    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName == null ? null : goodsName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_info.goods_count
     *
     * @return the value of order_info.goods_count
     *
     * @mbggenerated Wed Oct 30 18:48:53 CST 2019
     */
    public Integer getGoodsCount() {
        return goodsCount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_info.goods_count
     *
     * @param goodsCount the value for order_info.goods_count
     *
     * @mbggenerated Wed Oct 30 18:48:53 CST 2019
     */
    public void setGoodsCount(Integer goodsCount) {
        this.goodsCount = goodsCount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_info.goods_price
     *
     * @return the value of order_info.goods_price
     *
     * @mbggenerated Wed Oct 30 18:48:53 CST 2019
     */
    public BigDecimal getGoodsPrice() {
        return goodsPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_info.goods_price
     *
     * @param goodsPrice the value for order_info.goods_price
     *
     * @mbggenerated Wed Oct 30 18:48:53 CST 2019
     */
    public void setGoodsPrice(BigDecimal goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_info.order_channel
     *
     * @return the value of order_info.order_channel
     *
     * @mbggenerated Wed Oct 30 18:48:53 CST 2019
     */
    public Byte getOrderChannel() {
        return orderChannel;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_info.order_channel
     *
     * @param orderChannel the value for order_info.order_channel
     *
     * @mbggenerated Wed Oct 30 18:48:53 CST 2019
     */
    public void setOrderChannel(Byte orderChannel) {
        this.orderChannel = orderChannel;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_info.status
     *
     * @return the value of order_info.status
     *
     * @mbggenerated Wed Oct 30 18:48:53 CST 2019
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_info.status
     *
     * @param status the value for order_info.status
     *
     * @mbggenerated Wed Oct 30 18:48:53 CST 2019
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_info.create_date
     *
     * @return the value of order_info.create_date
     *
     * @mbggenerated Wed Oct 30 18:48:53 CST 2019
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_info.create_date
     *
     * @param createDate the value for order_info.create_date
     *
     * @mbggenerated Wed Oct 30 18:48:53 CST 2019
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_info.pay_date
     *
     * @return the value of order_info.pay_date
     *
     * @mbggenerated Wed Oct 30 18:48:53 CST 2019
     */
    public Date getPayDate() {
        return payDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_info.pay_date
     *
     * @param payDate the value for order_info.pay_date
     *
     * @mbggenerated Wed Oct 30 18:48:53 CST 2019
     */
    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }
}