package dic.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author ：aiyk
 * @date ：Created in 2022/8/20 16:05
 * @description：1
 * @modified By：
 */

//@Data
//@TableName("ssmp")
@Component
@Data
public class Ssmp extends Model<Ssmp> {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String head;
    private String name;
    private int sex;
    private int age;
    private String email;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthdate;

}
