package cn.elegent.token.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * TimeUnit
 * @author: wgl
 * @describe: ElegentToken框架提供的时间单位字典枚举类
 * @date: 2022/12/28 13:18
 **/
@Getter
public enum TimeUnit {
    DAY(0,"day"),//日
    HOURS(1,"hours"),//小时
    MINUTES(2,"Minutes"),//分钟
    SECONDS(3,"seconds")//秒
    ;


    private Integer code;
    private String name;

    /**
     * 获取有效期
     * @param expire
     * @param unit
     * @param zoneId
     * @return
     */
    public static ZonedDateTime getEndDate(int expire,TimeUnit unit,ZoneId zoneId){
        switch (unit){
            case DAY:
                return LocalDateTime.now().plusDays(expire).atZone(zoneId);
            case HOURS:
                return LocalDateTime.now().plusHours(expire).atZone(zoneId);
            case MINUTES:
                return LocalDateTime.now().plusMinutes(expire).atZone(zoneId);
            case SECONDS:
                return LocalDateTime.now().plusSeconds(expire).atZone(zoneId);
            default:
                return null;
        }
    }

    TimeUnit(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
