/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;
import org.junit.Test;

import java.time.Instant;
import java.util.*;

/**
 * Tests for SocialNetwork class.
 */
public class SocialNetworkTest {

	private static final Instant time1 = Instant.parse("2020-01-01T10:00:00Z");
	private static final Instant time2 = Instant.parse("2020-01-01T11:00:00Z");

	/**
	 * 1. Empty List of Tweets: Ensures that passing an empty list creates an empty
	 * graph.
	 */
	@Test
	public void testGuessFollowsGraphEmptyList() {
		List<Tweet> tweets = new ArrayList<>();
		Map<String, Set<String>> graph = SocialNetwork.guessFollowsGraph(tweets);
		assertTrue(graph.isEmpty());
	}

	/**
	 * 2. Tweets Without Mentions: Verifies that tweets with no @-mentions do not
	 * add any entries to the graph.
	 */
	@Test
	public void testGuessFollowsGraphNoMentions() {
		Tweet t1 = new Tweet(1, "Alice", "Hello everyone!", time1);
		List<Tweet> tweets = Arrays.asList(t1);
		Map<String, Set<String>> graph = SocialNetwork.guessFollowsGraph(tweets);
		assertTrue(graph.isEmpty());
	}

	/**
	 * 3. Single Mention: Tests that a user who mentions someone creates a follow
	 * link.
	 */
	@Test
	public void testGuessFollowsGraphSingleMention() {
		Tweet t1 = new Tweet(1, "Alice", "Hello @Bob", time1);
		List<Tweet> tweets = Arrays.asList(t1);
		Map<String, Set<String>> graph = SocialNetwork.guessFollowsGraph(tweets);
		assertTrue(graph.containsKey("alice"));
		assertTrue(graph.get("alice").contains("bob"));
	}

	/**
	 * 4. Multiple Mentions: Checks that multiple mentions from one tweet are added
	 * to the graph.
	 */
	@Test
	public void testGuessFollowsGraphMultipleMentions() {
		Tweet t1 = new Tweet(1, "Alice", "@Bob @Charlie @Dave", time1);
		List<Tweet> tweets = Arrays.asList(t1);
		Map<String, Set<String>> graph = SocialNetwork.guessFollowsGraph(tweets);
		assertTrue(graph.get("alice").contains("bob"));
		assertTrue(graph.get("alice").contains("charlie"));
		assertTrue(graph.get("alice").contains("dave"));
		assertEquals(3, graph.get("alice").size());
	}

	/**
	 * 5. Multiple Tweets from One User: Ensures that repeated mentions from the
	 * same user are merged correctly.
	 */
	@Test
	public void testGuessFollowsGraphMultipleTweetsSameUser() {
		Tweet t1 = new Tweet(1, "Alice", "@Bob", time1);
		Tweet t2 = new Tweet(2, "Alice", "@Charlie", time2);
		List<Tweet> tweets = Arrays.asList(t1, t2);
		Map<String, Set<String>> graph = SocialNetwork.guessFollowsGraph(tweets);
		assertTrue(graph.get("alice").contains("bob"));
		assertTrue(graph.get("alice").contains("charlie"));
		assertEquals(2, graph.get("alice").size());
	}

	/**
	 * 6. Empty Graph for influencers(): If the graph is empty, the influencer list
	 * should be empty too.
	 */
	@Test
	public void testInfluencersEmptyGraph() {
		Map<String, Set<String>> graph = new HashMap<>();
		List<String> influencers = SocialNetwork.influencers(graph);
		assertTrue(influencers.isEmpty());
	}

	/**
	 * 7. Single User Without Followers: If no one is followed, there should be no
	 * influencers.
	 */
	@Test
	public void testInfluencersSingleUserNoFollowers() {
		Map<String, Set<String>> graph = new HashMap<>();
		graph.put("alice", new HashSet<>()); // Alice follows no one
		List<String> influencers = SocialNetwork.influencers(graph);
		assertTrue(influencers.isEmpty());
	}

	/**
	 * 8. Single Influencer: Tests that the only mentioned person is correctly
	 * identified as the top influencer.
	 */
	@Test
	public void testInfluencersSingleInfluencer() {
		Map<String, Set<String>> graph = new HashMap<>();
		graph.put("alice", new HashSet<>(Arrays.asList("bob")));
		List<String> influencers = SocialNetwork.influencers(graph);
		assertEquals(1, influencers.size());
		assertEquals("bob", influencers.get(0));
	}

	/**
	 * 9. Multiple Influencers: Tests that influencer list is sorted by number of
	 * followers.
	 */
	@Test
	public void testInfluencersMultipleInfluencers() {
		Map<String, Set<String>> graph = new HashMap<>();
		graph.put("alice", new HashSet<>(Arrays.asList("bob", "charlie")));
		graph.put("dave", new HashSet<>(Arrays.asList("bob")));
		List<String> influencers = SocialNetwork.influencers(graph);
		assertEquals("bob", influencers.get(0)); // bob has 2 followers
		assertEquals("charlie", influencers.get(1)); // charlie has 1 follower
	}

	/**
	 * 10. Tied Influence: Ensures that users with the same number of followers both
	 * appear in the list. Order between tied users is not strictly required.
	 */
	@Test
	public void testInfluencersTiedInfluence() {
		Map<String, Set<String>> graph = new HashMap<>();
		graph.put("alice", new HashSet<>(Arrays.asList("bob")));
		graph.put("charlie", new HashSet<>(Arrays.asList("dave")));
		List<String> influencers = SocialNetwork.influencers(graph);
		assertTrue(influencers.contains("bob"));
		assertTrue(influencers.contains("dave"));
	}
}
