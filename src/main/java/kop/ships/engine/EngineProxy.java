package kop.ships.engine;

/**
 * @author Ola Sundell
 */
public class EngineProxy {
	private Engine delegate;

	public EngineProxy(Engine engine) {
		this.delegate = engine;
	}
}
