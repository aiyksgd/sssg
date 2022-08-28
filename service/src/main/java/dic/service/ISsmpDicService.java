package dic.service;


import com.baomidou.mybatisplus.extension.service.IService;
import dic.entity.Ssmp;

import java.util.List;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ayk
 * @since 2021-12-02
 */
public interface ISsmpDicService extends IService<Ssmp> {

     public List<Ssmp> select();
	 
	 public void add(Ssmp dic);
		
	 public Ssmp getData(Integer id);
		
	 public void remove(Integer id);
		
	 public void update(Ssmp dic);
	 
	 public String selectPage(int current, int size);//分页查询
			
	 public void selectCount(int pageNo); //分页
			
		
	 
	 
	
	 
}
