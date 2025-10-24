using System.ComponentModel.DataAnnotations;

namespace BrokerApi.Data.Entities;

public class Account
{
    [Key]
    public string PeerId { get; set; } = string.Empty;
    public double Balance { get; set; }
}

public class Pricing
{
    [Key]
    public string PeerId { get; set; } = string.Empty;
    public double PricePer1kTokens { get; set; }
    public double MinQueryPrice { get; set; }
}

public class MeteringRecord
{
    [Key]
    public Guid Id { get; set; } = Guid.NewGuid();
    public string Provider { get; set; } = string.Empty;
    public string Consumer { get; set; } = string.Empty;
    public string QueryId { get; set; } = string.Empty;
    public int TokensOut { get; set; }
    public double UnitPrice { get; set; }
    public DateTimeOffset CreatedAt { get; set; } = DateTimeOffset.UtcNow;
}

public class Payment
{
    [Key]
    public Guid Id { get; set; } = Guid.NewGuid();
    public string PeerId { get; set; } = string.Empty;
    public double Amount { get; set; }
    public DateTimeOffset CreatedAt { get; set; } = DateTimeOffset.UtcNow;
}

public class PaymentRequest
{
    public string PeerId { get; set; } = string.Empty;
    public double Amount { get; set; }
}
