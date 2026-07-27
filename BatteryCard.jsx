function BatteryCard({ data }) {
  return (
    <div className="card">
      <h2>🔋 Battery</h2>
      <h3>{data.level}</h3>
      <p>Status: {data.status}</p>
    </div>
  );
}

export default BatteryCard;