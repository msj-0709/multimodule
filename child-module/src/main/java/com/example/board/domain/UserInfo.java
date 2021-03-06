package com.example.board.domain;


import com.example.board.constant.UserStatusType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;


import javax.persistence.*;
import java.util.List;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="userinfo")
@DynamicUpdate
@DynamicInsert
public class UserInfo {
    @Id
    @Column(name="userinfoid")
    private String userInfoId;

    @Column(updatable = false)
    private String username;

    @Column
    private String password;

    @Column
    private UserStatusType status;

    @Column(name="koreanname")
    private String koreanName;


    //연관관계의 주인은 mapped By 옵션을 사용하지 않는다
    //이때 양방향 연관관계일 경우, 연관관계의 주인은 외래키를 가지고 있는 쪽으로 생각하면 된다.
    //FK가 있는 곳을 주인으로 한다.
    @OneToMany(mappedBy = "userInfo")
    private List<Board> boards;

}