using System;
using BrokerApi.Data;
using BrokerApi.Data.Entities;
using Microsoft.AspNetCore.Mvc;

namespace BrokerApi.Controllers;

[ApiController]
[Route("payments")]
public class PaymentsController : ControllerBase
{
    private readonly BrokerDb _db;

    public PaymentsController(BrokerDb db) => _db = db;

    [HttpPost("topup")]
    public async Task<IActionResult> TopUp(PaymentRequest request)
    {
        var account = _db.Accounts.FirstOrDefault(a => a.PeerId == request.PeerId);
        if (account is null)
        {
            return NotFound();
        }

        account.Balance += request.Amount;
        _db.Payments.Add(new Payment
        {
            Id = Guid.NewGuid(),
            PeerId = request.PeerId,
            Amount = request.Amount,
            CreatedAt = DateTimeOffset.UtcNow
        });

        await _db.SaveChangesAsync();
        return Ok(account);
    }
}
