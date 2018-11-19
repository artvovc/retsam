package brokers.common

import java.lang.management.ManagementFactory

trait PidExtractor {

  def pid = ManagementFactory.getRuntimeMXBean.getName

}
