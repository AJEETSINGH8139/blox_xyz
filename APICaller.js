class APICaller {
    constructor() {
      this.tokenBucket = 15; // Number of tokens (API calls) available per minute
      this.queue = [];
      this.penaltyPeriod = 0; // The duration of the penalty period in milliseconds
    }
  
    async call_me(input) {
      // Wait until a token becomes available in the token bucket
      while (this.tokenBucket <= 0) {
        await this.wait(1000); // Wait for 1 second and check again
      }
  
      // Consume one token (make the API call)
      this.tokenBucket--;
  
      if (this.penaltyPeriod > 0) {
        // During penalty period, do not make any API call
        return "Penalty period: No API calls allowed.";
      }
  
      try {
        const result = await this.makeAPICall(input);
        return result;
      } catch (error) {
        // Handle API call error
        return "API call failed: " + error.message;
      }
    }
  
    async makeAPICall(input) {
      // Simulate the actual API call here
      console.log("Making API call with input:", input);
      return "API response";
    }
  
    async wait(ms) {
      return new Promise((resolve) => setTimeout(resolve, ms));
    }
  }
  
  // Example usage:
  const apiCaller = new APICaller();
  
  // Make 15 API calls within a minute
  for (let i = 0; i < 15; i++) {
    apiCaller.call_me("Input for API call " + (i + 1));
  }
  
  // After the 15th call, there will be a 1-minute penalty period
  // during which no API calls are allowed.
  apiCaller.call_me("API call after exceeding limit").then((response) => {
    console.log(response);
  });
  