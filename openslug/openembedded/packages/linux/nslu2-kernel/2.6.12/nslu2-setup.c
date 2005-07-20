/*
 * arch/arm/mach-ixp4xx/nslu2-setup.c
 *
 * NSLU2 board-setup
 *
 * based ixdp425-setup.c:
 *      Copyright (C) 2003-2004 MontaVista Software, Inc.
 *
 * Author: Mark Rakes <mrakes at mac.com>
 * Maintainers: http://www.nslu2-linux.org/
 *
 * Fixed missing init_time in MACHINE_START kas11 10/22/04
 * Changed to conform to new style __init ixdp425 kas11 10/22/04 
 */

#include <linux/kernel.h>
#include <linux/init.h>
#include <linux/device.h>
#include <linux/serial.h>
#include <linux/tty.h>
#include <linux/serial_8250.h>

#include <asm/types.h>
#include <asm/setup.h>
#include <asm/memory.h>
#include <asm/hardware.h>
#include <asm/mach-types.h>
#include <asm/irq.h>
#include <asm/mach/arch.h>
#include <asm/mach/flash.h>

void __init nslu2_map_io(void) 
{
	ixp4xx_map_io();
}

static struct flash_platform_data nslu2_flash_data = {
	.map_name	= "cfi_probe",
	.width		= 2,
};

static struct resource nslu2_flash_resource = {
	.start		= NSLU2_FLASH_BASE,
	.end		= NSLU2_FLASH_BASE + NSLU2_FLASH_SIZE,
	.flags		= IORESOURCE_MEM,
};

static struct platform_device nslu2_flash = {
	.name		= "IXP4XX-Flash",
	.id		= 0,
	.dev		= {
		.platform_data = &nslu2_flash_data,
	},
	.num_resources	= 1,
	.resource	= &nslu2_flash_resource,
};

static struct ixp4xx_i2c_pins nslu2_i2c_gpio_pins = {
	.sda_pin	= NSLU2_SDA_PIN,
	.scl_pin	= NSLU2_SCL_PIN,
};

static struct platform_device nslu2_i2c_controller = {
	.name		= "IXP4XX-I2C",
	.id		= 0,
	.dev		= {
		.platform_data = &nslu2_i2c_gpio_pins,
	},
	.num_resources	= 0
};

static struct resource nslu2_uart_resources[] = {
	{
		.start		= IXP4XX_UART1_BASE_PHYS,
		.end		= IXP4XX_UART1_BASE_PHYS + 0x0fff,
		.flags		= IORESOURCE_MEM
	},
	{
	},
#if 0 
	, {
		.start		= IXP4XX_UART2_BASE_PHYS,
		.end		= IXP4XX_UART2_BASE_PHYS + 0x0fff,
		.flags		= IORESOURCE_MEM
	}
#endif
};

static struct plat_serial8250_port nslu2_uart_data[] = {
	{
		.mapbase	= IXP4XX_UART1_BASE_PHYS,
		.membase	= (char *)IXP4XX_UART1_BASE_VIRT + REG_OFFSET,
		.irq		= IRQ_IXP4XX_UART1,
		.flags		= UPF_BOOT_AUTOCONF,
		.iotype		= UPIO_MEM,
		.regshift	= 2,
		.uartclk	= IXP4XX_UART_XTAL,
	},
	{
	},

#if 0
	, {
		.mapbase	= IXP4XX_UART2_BASE_PHYS,
		.membase	= (char *)IXP4XX_UART2_BASE_VIRT + REG_OFFSET,
		.irq		= IRQ_IXP4XX_UART1,
		.flags		= UPF_BOOT_AUTOCONF,
		.iotype		= UPIO_MEM,
		.regshift	= 2,
		.uartclk	= IXP4XX_UART_XTAL,
	}
#endif
};

static struct platform_device nslu2_uart = {
	.name			= "serial8250",
	.id			= 0,
	.dev.platform_data	= nslu2_uart_data,
	.num_resources		= 2,
	.resource		= nslu2_uart_resources
};

static struct platform_device *nslu2_devices[] __initdata = {
	&nslu2_i2c_controller,
	&nslu2_flash,
	&nslu2_uart
};

static void n2_power_off(void)
{
        /* This causes the box to drop the power and go dead. */
#define GPIO_PO_BM              0x0100  //b0000 0001 0000 0000
        *IXP4XX_GPIO_GPOER &= ~GPIO_PO_BM;      // enable the pwr cntl gpio
        *IXP4XX_GPIO_GPOUTR |= GPIO_PO_BM;      // do the deed
}

static void __init nslu2_init(void)
{
	ixp4xx_sys_init();

	pm_power_off = n2_power_off;
	platform_add_devices(nslu2_devices, ARRAY_SIZE(nslu2_devices));
}

MACHINE_START(NSLU2, "Linksys NSLU2")
        MAINTAINER("www.nslu2-linux.org")
	        BOOT_MEM(PHYS_OFFSET, IXP4XX_PERIPHERAL_BASE_PHYS, 
			IXP4XX_PERIPHERAL_BASE_VIRT)
	        MAPIO(nslu2_map_io)
	        INITIRQ(ixp4xx_init_irq)        //FIXME: all irq are off here
	        .timer          = &ixp4xx_timer,
	        // INITTIME(ixp4xx_init_time)   //this was missing in 2.6.7 code ...soft reboot needed?
		BOOT_PARAMS(0x0100)
		INIT_MACHINE(nslu2_init)
MACHINE_END

