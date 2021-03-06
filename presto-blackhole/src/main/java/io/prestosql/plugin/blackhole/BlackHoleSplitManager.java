/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.prestosql.plugin.blackhole;

import com.google.common.collect.ImmutableList;
import io.prestosql.spi.connector.ConnectorSession;
import io.prestosql.spi.connector.ConnectorSplitManager;
import io.prestosql.spi.connector.ConnectorSplitSource;
import io.prestosql.spi.connector.ConnectorTableLayoutHandle;
import io.prestosql.spi.connector.ConnectorTransactionHandle;
import io.prestosql.spi.connector.FixedSplitSource;

public final class BlackHoleSplitManager
        implements ConnectorSplitManager
{
    @Override
    public ConnectorSplitSource getSplits(ConnectorTransactionHandle transactionHandle, ConnectorSession session, ConnectorTableLayoutHandle layoutHandle, SplitSchedulingStrategy splitSchedulingStrategy)
    {
        BlackHoleTableLayoutHandle layout = (BlackHoleTableLayoutHandle) layoutHandle;

        ImmutableList.Builder<BlackHoleSplit> builder = ImmutableList.builder();

        for (int i = 0; i < layout.getSplitCount(); i++) {
            builder.add(
                    new BlackHoleSplit(
                            layout.getPagesPerSplit(),
                            layout.getRowsPerPage(),
                            layout.getFieldsLength(),
                            layout.getPageProcessingDelay()));
        }
        return new FixedSplitSource(builder.build());
    }
}
