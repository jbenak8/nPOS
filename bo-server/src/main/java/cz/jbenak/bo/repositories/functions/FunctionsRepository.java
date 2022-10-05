package cz.jbenak.bo.repositories.functions;

import cz.jbenak.bo.models.Function;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;

@Repository
public interface FunctionsRepository extends ReactiveCrudRepository<Function, String> {

    @Query("SELECT * FROM functions ORDER BY id ASC;")
    Flux<Function> getAllFunctions();

    @Query("SELECT * FROM functions INNER JOIN functions_mapping fm ON functions.id = fm.function_id WHERE fm.group_id = :groupId")
    Flux<Function> getFunctionsByGroup(int groupId);

    @Query("""
            SELECT * FROM functions 
            INNER JOIN functions_mapping fm ON functions.id = fm.function_id 
            WHERE fm.group_id = :groupId AND key_component = :component
            """)
    Flux<Function> getFunctionsByGroupAndComponent(int groupId, Function.KeyComponent component);

    @Query("""
            SELECT * FROM functions 
                        INNER JOIN functions_mapping fm ON functions.id = fm.function_id 
                        WHERE fm.group_id IN (:groupIdsIn) AND key_component = :component
            """)
    Flux<Function> getFunctionsByGroupsAndComponent(List<Integer> groupIdsIn, Function.KeyComponent component);
}
