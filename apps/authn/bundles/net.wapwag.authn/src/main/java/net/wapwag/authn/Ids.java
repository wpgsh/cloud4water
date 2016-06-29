package net.wapwag.authn;

public class Ids {

	public static class UserId {
		
		public final long id;
		
		public UserId(long id) {
			if (id < 0) {
				throw new IllegalArgumentException("Negative user id");
			}
			this.id = id;
		}
		
		public String toString() {
			return String.format("uX%016x", id);
		}
		
		public static UserId fromString(String id) {
			if (id.startsWith("uX") && id.length() == 18) {
				try {
					return new UserId(Long.parseLong(id.substring(2), 16));
				} catch (NumberFormatException e) {
					throw new IllegalArgumentException("Invalid user id: "+id);
				}
			} else {
				throw new IllegalArgumentException("Invalid user id: "+id);
			}
		}
	}

}
