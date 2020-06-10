package com.cn.boot.sample.api.model.po;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

/**
 * 敏感词
 */
@Table(name = "room")
@Entity
@Data
public class Room implements Serializable {
  private static final long serialVersionUID = 1L;

  /**
   * 主键
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", insertable = false, nullable = false)
  private Long id;

  /**
   * 名称
   */
  @Column(name = "name", nullable = false)
  private String name;

  /**
   * 应用id
   */
  @Column(name = "appId", nullable = false)
  private String appId = "";

  
}