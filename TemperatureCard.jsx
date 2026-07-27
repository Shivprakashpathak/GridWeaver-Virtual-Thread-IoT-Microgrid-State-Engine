

function TemperatureCard({ data }) {
  return (
    <div className="card">
      <h2>🌡 Temperature</h2>
      <h3>{data.value}</h3>
      <p>Environment: {data.environment}</p>
    </div>
  );
}

export default TemperatureCard;