package server.rules;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Nemanja Srdanovic
 *
 */
public class Rules {

	private List<IRule> rules;

	public Rules() {

		this.rules = new ArrayList<IRule>();

		this.rules.add(new BorderRuleCheck());
		this.rules.add(new CastleRuleCheck());
		this.rules.add(new GameRuleCheck());
		this.rules.add(new IslandRuleCheck());
		this.rules.add(new PlayerRuleCheck());
		this.rules.add(new TerrainRuleCheck());
		this.rules.add(new TooMuchMapsSentRuleCheck());

	}

	public List<IRule> getRules() {
		return rules;
	}

}
