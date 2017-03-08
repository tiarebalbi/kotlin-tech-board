package com.tiarebalbi.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class TopicTest {

  @Test
  fun `Should Create a topic with slug valid`() {
    // given
    val name = "Custom High Speed Bus"

    // when
    val topic = getTopic(name)

    // then
    assertThat(topic.slug).isEqualTo("custom-high-speed-bus")
    assertThat(topic.color).isEqualTo(Color.GREY)
    assertThat(topic.name).isEqualTo(name)
    assertThat(topic.description).isEmpty()
  }

  @Test
  fun `Should update the slug of a topic when someone change the name`() {
    val topic = getTopic(name = "New Topic")
    topic.name = "New Name"

    assertThat(topic.name).isEqualTo("New Name")
    assertThat(topic.slug).isEqualTo("new-name")
  }

  @Test
  fun `Should ignore accepts in the slug`() {
    val topic = getTopic(name = "Tésting Açééóá")
    assertThat(topic.name).isEqualTo("Tésting Açééóá")
    assertThat(topic.slug).isEqualTo("testing-aceeoa")
  }


  private fun getTopic(name: String) = Topic(name)

}