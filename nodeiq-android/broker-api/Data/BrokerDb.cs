using BrokerApi.Data.Entities;
using Microsoft.EntityFrameworkCore;

namespace BrokerApi.Data;

public class BrokerDb : DbContext
{
    public BrokerDb(DbContextOptions<BrokerDb> options) : base(options) { }

    public DbSet<Account> Accounts => Set<Account>();
    public DbSet<Pricing> Pricing => Set<Pricing>();
    public DbSet<MeteringRecord> Metering => Set<MeteringRecord>();
    public DbSet<Payment> Payments => Set<Payment>();

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        modelBuilder.Entity<Account>().HasKey(a => a.PeerId);
        modelBuilder.Entity<Pricing>().HasKey(p => p.PeerId);
        modelBuilder.Entity<MeteringRecord>().HasKey(m => m.Id);
        modelBuilder.Entity<Payment>().HasKey(p => p.Id);
    }
}

public static class DbSetExtensions
{
    public static void Upsert<T>(this DbSet<T> set, T entity) where T : class
    {
        if (set.Local.Contains(entity))
        {
            set.Update(entity);
        }
        else
        {
            set.Update(entity);
        }
    }
}
