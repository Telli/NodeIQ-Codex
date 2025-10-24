package ai.nodeiq.mqtt

data class TopicConfig(
    val presence: String,
    val queryTemplate: String,
    val responseTemplate: String
) {
    fun queryTopic(agentId: String): String = queryTemplate.replace("{agent_id}", agentId)
    fun responseTopic(queryId: String): String = responseTemplate.replace("{query_id}", queryId)
}
