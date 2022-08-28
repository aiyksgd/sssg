package dic.service.impl;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dic.entity.Ssmp;
import dic.mapper.SsmpDicMapper;
import dic.service.ISsmpDicService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * </p>
 *
 * @author ayk
 * @since 2021-12-02
 */
@Service
public class SsmpDicServiceImpl extends ServiceImpl<SsmpDicMapper, Ssmp> implements ISsmpDicService {
    private static final Wrapper<Ssmp> QueryWrapper = null;
    @Autowired
    private Ssmp dic;


    @Override
    public List<Ssmp> select() {
        List<Ssmp> list = dic.selectAll();

        return list;
    }

    @Override
    public void add(Ssmp dic) {
        dic.insert();
    }


    @Override
    public Ssmp getData(Integer id) {
        return dic.selectById(id);
    }


    @Override
    public void remove(Integer id) {
        dic.deleteById(id);
    }


    @Override
    public void update(Ssmp dd) {
        dd.updateById();
    }


    @Override
    public String selectPage(int current, int size) {
//		System.out.println(current+""+size);
//		Page<Ssmp> p =new Page<Ssmp>(current,size);
        return "好";
    }


    @Override
    public void selectCount(int pageNo) {

    }


}
