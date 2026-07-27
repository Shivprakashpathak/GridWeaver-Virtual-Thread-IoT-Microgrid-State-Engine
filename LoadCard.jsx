function LoadCard({ data }) {
  return (
    <div className="card">
      <h2>⚡ Load Demand</h2>
      <h3>{data.demand}</h3>
      <p>Status: {data.status}</p>
    </div>
  );
}

export default LoadCard;