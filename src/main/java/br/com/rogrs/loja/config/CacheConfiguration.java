package br.com.rogrs.loja.config;

import io.github.jhipster.config.JHipsterProperties;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.jsr107.Eh107Configuration;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
@AutoConfigureAfter(value = { MetricsConfiguration.class })
@AutoConfigureBefore(value = { WebConfigurer.class, DatabaseConfiguration.class })
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(Expirations.timeToLiveExpiration(Duration.of(ehcache.getTimeToLiveSeconds(), TimeUnit.SECONDS)))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache("users", jcacheConfiguration);
            cm.createCache(br.com.rogrs.loja.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.rogrs.loja.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.rogrs.loja.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(br.com.rogrs.loja.domain.Localidades.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.rogrs.loja.domain.Localidades.class.getName() + ".cadastrosLocalidades", jcacheConfiguration);
            cm.createCache(br.com.rogrs.loja.domain.Cadastros.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.rogrs.loja.domain.Cadastros.class.getName() + ".cadastrosLocalidades", jcacheConfiguration);
            cm.createCache(br.com.rogrs.loja.domain.Cadastros.class.getName() + ".pedidos", jcacheConfiguration);
            cm.createCache(br.com.rogrs.loja.domain.CadastrosLocalidades.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.rogrs.loja.domain.Categorias.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.rogrs.loja.domain.Categorias.class.getName() + ".produtos", jcacheConfiguration);
            cm.createCache(br.com.rogrs.loja.domain.Marcas.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.rogrs.loja.domain.Marcas.class.getName() + ".produtos", jcacheConfiguration);
            cm.createCache(br.com.rogrs.loja.domain.Tamanhos.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.rogrs.loja.domain.Tamanhos.class.getName() + ".produtos", jcacheConfiguration);
            cm.createCache(br.com.rogrs.loja.domain.Cores.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.rogrs.loja.domain.Cores.class.getName() + ".produtos", jcacheConfiguration);
            cm.createCache(br.com.rogrs.loja.domain.Produtos.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.rogrs.loja.domain.Produtos.class.getName() + ".itens", jcacheConfiguration);
            cm.createCache(br.com.rogrs.loja.domain.Pedidos.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.rogrs.loja.domain.Pedidos.class.getName() + ".itens", jcacheConfiguration);
            cm.createCache(br.com.rogrs.loja.domain.Itens.class.getName(), jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
