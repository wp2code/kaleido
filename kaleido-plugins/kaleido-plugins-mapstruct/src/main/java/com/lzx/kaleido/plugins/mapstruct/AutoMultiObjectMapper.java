package com.lzx.kaleido.plugins.mapstruct;

import io.github.zhaord.mapstruct.plus.AutoMapperFactory;
import io.github.zhaord.mapstruct.plus.BaseAutoMapper;
import io.github.zhaord.mapstruct.plus.IObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author lwp
 * @date 2023-11-18
 **/
public record AutoMultiObjectMapper(AutoMapperFactory factory) implements IObjectMapper {
    
    @Override
    public <S, D> D map(S source, Class<?> destinationCls) {
        if (source == null) {
            return null;
        }
        BaseAutoMapper<S, D> autoMapper = factory.find(source.getClass(), destinationCls);
        
        if (autoMapper != null) {
            return autoMapper.map(source);
            
        }
        BaseAutoMapper<D, S> revAutoMapper = factory.find(destinationCls, source.getClass());
        if (revAutoMapper != null) {
            return revAutoMapper.reverseMap(source);
        }
        
        throw new RuntimeException(
                "no mapping from " + source.getClass().getName() + " to " + destinationCls.getName() + ". check @AutoMap on "
                        + source.getClass().getName() + " or " + destinationCls.getName());
    }
    
    @Override
    public <S, D> D map(S source, D destination) {
        if (source == null) {
            return null;
        }
        BaseAutoMapper<S, D> autoMapper = factory.find(source.getClass(), destination.getClass());
        
        if (autoMapper != null) {
            return autoMapper.mapTarget(source, destination);
            
        }
        BaseAutoMapper<D, S> revAutoMapper = factory.find(destination.getClass(), source.getClass());
        if (revAutoMapper != null) {
            return revAutoMapper.reverseMapTarget(source, destination);
        }
        
        throw new RuntimeException(
                "no mapping from " + source.getClass().getName() + " to " + destination.getClass().getName() + ". check @AutoMap on "
                        + source.getClass().getName() + " or " + destination.getClass().getName());
        
    }
    
    @Override
    public <S, D> List<D> mapList(List<S> source, Class<?> destinationCls) {
        if (Objects.isNull(source)) {
            return null;
        }
        if (source.size() == 0) {
            return new ArrayList<>();
        }
        final S s1 = source.get(0);
        BaseAutoMapper<S, D> autoMapper = factory.find(s1.getClass(), destinationCls);
        if (autoMapper != null) {
            return source.stream().map(s -> autoMapper.map(s)).collect(Collectors.toList());
        }
        BaseAutoMapper<D, S> revAutoMapper = factory.find(destinationCls, s1.getClass());
        if (revAutoMapper != null) {
            return source.stream().map(s -> revAutoMapper.reverseMap(s)).collect(Collectors.toList());
        }
        throw new RuntimeException("no mapping from " + s1.getClass().getName() + " to " + destinationCls.getName() + ". check @AutoMap on "
                + source.getClass().getName() + " or " + destinationCls.getName());
    }
}
