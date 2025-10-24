using BrokerApi.Data;
using BrokerApi.Data.Entities;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace BrokerApi.Controllers;

[ApiController]
[Route("pricing")]
public class PricingController : ControllerBase
{
    private readonly BrokerDb _db;

    public PricingController(BrokerDb db) => _db = db;

    [HttpPost("{peerId}")]
    public async Task<IActionResult> Upsert(string peerId, Pricing pricing)
    {
        pricing.PeerId = peerId;
        _db.Pricing.Upsert(pricing);
        await _db.SaveChangesAsync();
        return Ok(pricing);
    }

    [HttpGet("{peerId}")]
    public async Task<IActionResult> Get(string peerId)
    {
        var pricing = await _db.Pricing.FirstOrDefaultAsync(p => p.PeerId == peerId);
        return pricing is null ? NotFound() : Ok(pricing);
    }
}
