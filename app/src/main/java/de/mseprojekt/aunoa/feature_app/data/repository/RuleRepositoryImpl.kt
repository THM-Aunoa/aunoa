package de.mseprojekt.aunoa.feature_app.data.repository

import de.mseprojekt.aunoa.feature_app.data.data_source.RuleDao
import de.mseprojekt.aunoa.feature_app.data.data_source.relations.RuleWithActAndTrig
import de.mseprojekt.aunoa.feature_app.data.data_source.relations.RuleWithTags
import de.mseprojekt.aunoa.feature_app.domain.model.*
import de.mseprojekt.aunoa.feature_app.domain.repository.RuleRepository
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.Callable
import java.util.concurrent.Executors

class RuleRepositoryImpl(
    private val dao: RuleDao
): RuleRepository {
    override suspend fun getMaxIdFromRules(): Int? {
        return dao.getMaxIdFromRules()
    }

    override suspend fun getRuleById(id: Int): RuleWithActAndTrig? {
        return dao.getRuleById(id);
    }
    override fun getRules(): Flow<List<RuleWithActAndTrig>> {
        return dao.getRules();
    }

    override fun getRulesWithTags(): Flow<List<RuleWithTags>> {
        return dao.getRulesWithTags();
    }

    override fun getRulesWithoutFlow(): List<RuleWithActAndTrig> {
        val callable = Callable{ dao.getRulesWithoutFlow() }

        val future = Executors.newSingleThreadExecutor().submit(callable)

        return future!!.get()
    }

    override fun getRulesWithTagsWithoutFlow(): List<RuleWithTags> {
        val callable = Callable{ dao.getRulesWithTagsWithoutFlow() }

        val future = Executors.newSingleThreadExecutor().submit(callable)

        return future!!.get()
    }

    override suspend fun insertRule(rule: Rule) {
        return dao.insertRule(rule)
    }

    override suspend fun insertTrigger(trigger: Trig) {
        return dao.insertTrigger(trigger)
    }

    override suspend fun insertAction(action: Act) {
        return dao.insertAction(action)
    }

    override suspend fun deleteRule(ruleId: Int) {
        return dao.deleteRule(ruleId)
    }

    override suspend fun setActive(active: Boolean, id: Int){
        return dao.setActive(active, id)
    }

    override suspend fun setEnabled(enabled: Boolean, id: Int){
        return dao.setEnabled(enabled, id)
    }

    override fun insertTag(tag: Tag){
        dao.insertTag(tag)
    }

    override fun insertTags(tags: List<Tag>) : List<Tag>{
        val result = ArrayList<Tag>()
        val currentTags= this.getTagsWithoutFlow()
        for (tag in tags){
            for (currentTag in currentTags){
                if (currentTag.title == tag.title){
                    continue
                }
            }
            this.insertTag(
                Tag(
                    title = tag.title
                )
            )
            result.add(this.getTagByName(tag.title))
        }
        return result
    }

    override suspend fun insertRuleTagCrossRef(ruleTagCrossRef : RuleTagCrossRef){
        dao.insertRuleTagCrossRef(ruleTagCrossRef)
    }
    override fun getTags(): Flow<List<Tag>>{
        return dao.getTags()
    }
    override fun getTagByName(title : String): Tag{
        return dao.getTagByName(title)
    }

    override fun getTagsWithoutFlow(): List<Tag>{
        val callable = Callable{ dao.getTagsWithoutFlow() }

        val future = Executors.newSingleThreadExecutor().submit(callable)

        return future!!.get()
    }
}